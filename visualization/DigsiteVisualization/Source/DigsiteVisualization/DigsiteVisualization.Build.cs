// Fill out your copyright notice in the Description page of Project Settings.

using UnrealBuildTool;

public class DigsiteVisualization : ModuleRules
{
	public DigsiteVisualization(ReadOnlyTargetRules Target) : base(Target)
	{
		PCHUsage = PCHUsageMode.UseExplicitOrSharedPCHs;
	
		PublicDependencyModuleNames.AddRange(new string[] { "Core", "CoreUObject", "Engine", "InputCore", "ProceduralMeshComponent" });

		PrivateDependencyModuleNames.AddRange(new string[] {  });

		PrivateDependencyModuleNames.AddRange(new string[] { "Slate", "SlateCore" });


		// Replace these paths with your local paths to the assimp_include folder
		PublicIncludePaths.Add("C:\\assimp_include\\include");
		PublicAdditionalLibraries.Add("C:\\assimp_include\\assimp-vc142-mt.lib");


		/*
		if (Target.Platform == UnrealTargetPlatform.Win64)
        {
			// Assimp include-folder
			PublicIncludePaths.Add("ThirdParty/assimp-5.0.0/include");
			// Assimp .lib file
			PublicAdditionalLibraries.Add("ThirdParty/assimp-5.0.0/assimp-vc142-mt.lib");
		}
		*/




		// Uncomment if you are using online features
		// PrivateDependencyModuleNames.Add("OnlineSubsystem");

		// To include OnlineSubsystemSteam, add it to the plugins section in your uproject file with the Enabled attribute set to true
	}
}
