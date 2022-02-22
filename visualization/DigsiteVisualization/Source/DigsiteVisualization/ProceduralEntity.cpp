// Fill out your copyright notice in the Description page of Project Settings.

#include "ProceduralEntity.h"

#include "ImageUtils.h"
#include "Materials/MaterialInstanceDynamic.h"
#include "UObject/ConstructorHelpers.h"



/**
* Creates a subobject from a ProceduralMeshComponent
* 
* BaseMaterial is fetched here
*/
AProceduralEntity::AProceduralEntity(): _currentMesh(0) {
	// reenable this if you want to reload the file, when it is modified
	// then put logic from "BeginPlay" into "Tick"
	//PrimaryActorTick.bCanEverTick = true;

	//setup baseMaterial (must be done in constructor, bc. of FObjectFinder)
	FString baseMaterialPath = "/Game/DigsiteVisualization/Materials/FG-BaseTexture";

	//texture path
	static ConstructorHelpers::FObjectFinder<UMaterial> material(*baseMaterialPath);
	if (material.Succeeded()) {
		baseMaterial = material.Object;
	}
	else {
		UE_LOG(LogTemp, Display, TEXT("Failed to find material"));
		//TODO maybe add default material
	}

	//create subobject
	_mesh = CreateDefaultSubobject<UProceduralMeshComponent>(TEXT("ProcMesh"));
	if (_mesh) {
		RootComponent = _mesh;
	}
}

/**
* Called after the ProceduralEntity is spawned
* 
* Contains overkill logic for reloading, if the mesh-file is modified during runtime.
* Checks for the lastModified-date and only reloads if mesh has changed since initial loading.
* If this functionality shall be reintroduced, move this logic to "AProceduralEntity::Tick()", and
* reenable "PrimaryActorTick.bCanEverTick = true;" in the constructor.
*/
void AProceduralEntity::BeginPlay() {	
	// loads a file from disk. Checks, whether the file was modified since loaded

	Super::BeginPlay();

	struct _stat buffer;
	std::string stdfilename(TCHAR_TO_UTF8(*_filePath));
	char* filename = &stdfilename[0];

	int result = _stat(filename, &buffer);
	if (result != 0) {
		UE_LOG(LogTemp, Display, TEXT("Problem getting fileinfo: %s"), *_filePath);
		switch (errno) {
		case ENOENT:
			UE_LOG(LogTemp, Display, TEXT("File not found."));
			break;
		case EINVAL:
			UE_LOG(LogTemp, Display, TEXT("Invalid parameter to _stat."));
			break;
		default:
			// anything else, should never happen!
			// reasons for this might be problems with the filepath, eg. symbolic / relative links
			// only use absolute paths!!!!!
			UE_LOG(LogTemp, Display, TEXT("Unexpected error."));
		}
	}
	else {
		if (_lastChangeDate == 0 || (int)buffer.st_mtime > _lastChangeDate) {
			_lastChangeDate = (int)buffer.st_mtime;
			UE_LOG(LogTemp, Display, TEXT("Loading the model"));
			loadModel(stdfilename);
		}
	}

}

/**
* Processes an individual mesh
* 
* Parses vertices, normals, uv-maps, etc.
*/
void AProceduralEntity::processMesh(aiMesh* mesh, const aiScene* scene) {
	UE_LOG(LogTemp, Display, TEXT("Processing a mesh"));

	// Create empty arrays for whole component
	if (_vertices.Num() <= _currentMesh) {
		_vertices.AddZeroed();
		_normals.AddZeroed();
		_uvs.AddZeroed();
		_tangents.AddZeroed();
		_vertexColors.AddZeroed();
		_indices.AddZeroed();
	}
	
	// create empty subarray for current mesh
	_vertices[_currentMesh].Empty();
	_normals[_currentMesh].Empty();
	_uvs[_currentMesh].Empty();
	_tangents[_currentMesh].Empty();
	_vertexColors[_currentMesh].Empty();
	_indices[_currentMesh].Empty();

	for (unsigned int i = 0; i < mesh->mNumVertices; i++) {

		FVector vertex, normal;
		// process vertex positions, normals and UVs
		vertex.X = - mesh->mVertices[i].x;	//negate, because Unreal uses Left-handed coordinate-cystem
		vertex.Y = mesh->mVertices[i].y;
		vertex.Z = mesh->mVertices[i].z;

		normal.X = mesh->mNormals[i].x;
		normal.Y = mesh->mNormals[i].y;
		normal.Z = mesh->mNormals[i].z;

		// if the mesh contains tex coords
		if (mesh->mTextureCoords[0]) {
			FVector2D uvs;
			uvs.X = mesh->mTextureCoords[0][i].x;
			uvs.Y = mesh->mTextureCoords[0][i].y;
			_uvs[_currentMesh].Add(uvs);
		}
		else {
			_uvs[_currentMesh].Add(FVector2D(0.f, 0.f));
		}
		_vertices[_currentMesh].Add(vertex);
		_normals[_currentMesh].Add(normal);
	}

	UE_LOG(LogTemp, Display, TEXT("Processed %d verticies"), mesh->mNumVertices);

	
	for (uint32 i = 0; i < mesh->mNumFaces; i++) {
		aiFace face = mesh->mFaces[i];
		_indices[_currentMesh].Add(face.mIndices[2]);
		_indices[_currentMesh].Add(face.mIndices[1]);
		_indices[_currentMesh].Add(face.mIndices[0]);
	}
	
	// create Mesh
	UE_LOG(LogTemp, Display, TEXT("Creating the mesh"));
	_mesh->CreateMeshSection(_currentMesh, _vertices[_currentMesh], _indices[_currentMesh], _normals[_currentMesh], _uvs[_currentMesh], _vertexColors[_currentMesh], _tangents[_currentMesh], true);
	

	UE_LOG(LogTemp, Display, TEXT("Finished creating the mesh"));

}

/**
* Processes all nodes and subsequent children in a given scene
*/
void AProceduralEntity::processNode(aiNode* node, const aiScene* scene) {
	UE_LOG(LogTemp, Display, TEXT("Processing a node"));
	// process all the node's meshes
	for (uint32 i = 0; i < node->mNumMeshes; i++) {
		UE_LOG(LogTemp, Display, TEXT("Found new mesh in node"));
		aiMesh* mesh = scene->mMeshes[node->mMeshes[i]];
		processMesh(mesh, scene);
		++_currentMesh;
	}

	// recurse to get all child elements in all nodes
	for (uint32 i = 0; i < node->mNumChildren; i++) {
		UE_LOG(LogTemp, Display, TEXT("Found new child in node"));
		processNode(node->mChildren[i], scene);
	}
}

/**
* Load the material from the scene
* 
* Texture files must lie in the same directory as the 3d-model-file.
*/

void AProceduralEntity::loadMaterial(const aiScene* scene) {
	// return if scene has no materials
	if (!scene->HasMaterials()) return; //TODO error message

	UE_LOG(LogTemp, Display, TEXT("Found new materials in scene"));
	for (unsigned int i = 0; i < scene->mNumMaterials; i++) {
		const aiMaterial* material = scene->mMaterials[i];

		unsigned int numTextures = material->GetTextureCount(aiTextureType_DIFFUSE);
		UE_LOG(LogTemp, Display, TEXT("Number of found textures: %d"), numTextures);

		// return if material has no textures
		if (material->GetTextureCount(aiTextureType_DIFFUSE) == 0) return; //TODO error message

		aiString tempTexturePath;
		FString TempTexturePath;
		// gets first material of all possible materials
		// possibly check for more materials in future
		material->GetTexture(aiTextureType_DIFFUSE, 0, &tempTexturePath);
		TempTexturePath = FString(tempTexturePath.C_Str());

		//create material based on baseMaterial
		UMaterialInstanceDynamic* sectionMaterial = UMaterialInstanceDynamic::Create(baseMaterial, this);

		std::string texturePath(TCHAR_TO_UTF8(*_filePath));
		std::size_t splitPosition = texturePath.find_last_of("/");
		// directory
		std::string dir = texturePath.substr(0, splitPosition);
		// file
		std::string file = texturePath.substr(splitPosition, texturePath.length());

		FString Fdir(dir.c_str());
		UE_LOG(LogTemp, Display, TEXT("Looking for texturefile in: %s"), *Fdir);

		UTexture2D* textureParam = FImageUtils::ImportFileAsTexture2D(Fdir + "/" + TempTexturePath); //add path in front
		// set diffuse parameter in base texture
		sectionMaterial->SetTextureParameterValue("Diffuse", textureParam);

		// apply material
		_mesh->SetMaterial(0, sectionMaterial);
	}
}

/**
* Use the ASSIMP-library to import the mesh(es) from the given 3d-file.
*/
void AProceduralEntity::loadModel(std::string path) {
	Assimp::Importer importer;

	UE_LOG(LogTemp, Display, TEXT("STARTING IMPORT OF MESH"));
	UE_LOG(LogTemp, Display, TEXT("Filename: %s"), *_filePath);

	// get scene from mesh, apply input flags for preprocessing (partially to counter UE4's left-handed coordinate-system)
	const aiScene* scene = importer.ReadFile(path, aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_GenNormals | aiProcess_FlipWindingOrder );

	if (!scene) return;

	loadMaterial(scene);

	_currentMesh = 0;
	processNode(scene->mRootNode, scene);
}
