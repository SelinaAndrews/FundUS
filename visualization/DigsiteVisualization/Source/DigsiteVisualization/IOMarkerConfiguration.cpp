// Fill out your copyright notice in the Description page of Project Settings.


#include "IOMarkerConfiguration.h"
#include "Misc/FileHelper.h"



void UIOMarkerConfiguration::saveFile(FString fileContent, FString filepath)
{
	FFileHelper::SaveStringToFile(fileContent, *filepath);
}

FString UIOMarkerConfiguration::readFile(FString filepath)
{
	FString fileContent;

	FFileHelper::LoadFileToString(fileContent, *filepath);

	return fileContent;
}
