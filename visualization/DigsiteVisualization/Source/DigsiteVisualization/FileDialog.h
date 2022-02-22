// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"
#include "GameFramework/Actor.h"
#include "FileDialog.generated.h"

UCLASS()
class DIGSITEVISUALIZATION_API AFileDialog : public AActor
{
	GENERATED_BODY()

public:
	// Sets default values for this actor's properties
	AFileDialog();


	UFUNCTION(BlueprintCallable, Category = "FileDialog")
		void OpenFileDialog(const FString& Title, const FString& DefaultFilePath, const FString& AllowedFileTypes, TArray<FString>& SelectedFiles);

protected:
	// Called when the game starts or when spawned
	virtual void BeginPlay() override;

public:
	// Called every frame
	virtual void Tick(float DeltaTime) override;

};
