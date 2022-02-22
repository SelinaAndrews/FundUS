// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"
#include "Kismet/BlueprintFunctionLibrary.h"
#include "IOMarkerConfiguration.generated.h"

/**
 * 
 */
UCLASS()
class DIGSITEVISUALIZATION_API UIOMarkerConfiguration : public UBlueprintFunctionLibrary
{
	GENERATED_BODY()


		UFUNCTION(BlueprintCallable, Category = IO)
		static void saveFile(FString fileContent, FString filepath);

		UFUNCTION(BlueprintCallable, Category = IO)
		static FString readFile(FString filepath);
	
};
