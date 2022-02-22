// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"
#include "Kismet/BlueprintFunctionLibrary.h"
#include "GetVersionNumber.generated.h"

/**
 * 
 */
UCLASS()
class DIGSITEVISUALIZATION_API UGetVersionNumber : public UBlueprintFunctionLibrary
{
	GENERATED_BODY()

		UFUNCTION(BlueprintCallable, Category = VersionNumber)
		static FString GetVersionNumber();
};
