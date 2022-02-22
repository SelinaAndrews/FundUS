// Fill out your copyright notice in the Description page of Project Settings.
#pragma once

#include <string>
#include <assimp/Importer.hpp>
#include <assimp/scene.h>
#include <assimp/postprocess.h>

#include "GameFramework/Actor.h"
#include "ProceduralMeshComponent.h"
#include "ProceduralEntity.generated.h"


UCLASS()
class DIGSITEVISUALIZATION_API AProceduralEntity : public AActor
{
	GENERATED_BODY()

public:
	UPROPERTY(EditAnywhere, BlueprintReadWrite, Category = "Generation")
		UProceduralMeshComponent* _mesh;

	UPROPERTY(EditAnywhere, BlueprintReadWrite, Category = "Generation")
		FString _filePath = "unicorn";

	AProceduralEntity();
	virtual void BeginPlay() override;

	UMaterial* baseMaterial;

private:
	int32 _currentMesh;
	int _lastChangeDate;

	TArray<TArray<FVector>> _vertices;
	TArray<TArray<int32>> _indices;
	TArray<TArray<FVector>> _normals;
	TArray<TArray<FVector2D>> _uvs;
	TArray<TArray<FProcMeshTangent>> _tangents;
	TArray<TArray<FColor>> _vertexColors;

	void processMesh(aiMesh* mesh, const aiScene* scene);
	void processNode(aiNode* node, const aiScene* scene);
	void loadModel(std::string path);
	void loadMaterial(const aiScene* scene);
};