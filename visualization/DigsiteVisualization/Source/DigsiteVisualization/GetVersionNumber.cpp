// Fill out your copyright notice in the Description page of Project Settings.

#include "GetVersionNumber.h"
#include "Misc/ConfigCacheIni.h"


FString UGetVersionNumber::GetVersionNumber()
{
    FString VersionNumber;
    GConfig->GetString(
        TEXT("/Script/EngineSettings.GeneralProjectSettings"),
        TEXT("ProjectVersion"),
        VersionNumber,
        GGameIni
    );
    return VersionNumber;
}
