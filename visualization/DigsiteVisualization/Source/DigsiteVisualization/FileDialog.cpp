// Fill out your copyright notice in the Description page of Project Settings.


#include "FileDialog.h"
#include "Developer/DesktopPlatform/Public/IDesktopPlatform.h"
#include "Developer/DesktopPlatform/Public/DesktopPlatformModule.h"

// Sets default values
AFileDialog::AFileDialog()
{
	// Set this actor to call Tick() every frame.  You can turn this off to improve performance if you don't need it.
	PrimaryActorTick.bCanEverTick = true;

}

void AFileDialog::OpenFileDialog(const FString& Title, const FString& DefaultFilePath, const FString& AllowedFileTypes, TArray<FString>& SelectedFiles)
{
	if (GEngine)
	{
		if (GEngine->GameViewport)
		{
			void* ParentWindowHandle = GEngine->GameViewport->GetWindow()->GetNativeWindow()->GetOSWindowHandle();
			IDesktopPlatform* DesktopPlatform = FDesktopPlatformModule::Get();
			if (DesktopPlatform)
			{
				//Opening the file picker!
				uint32 SelectionFlag = 0; //A value of 0 represents single file selection while a value of 1 represents multiple file selection
				DesktopPlatform->OpenFileDialog(ParentWindowHandle, Title, DefaultFilePath, FString(""), AllowedFileTypes, SelectionFlag, SelectedFiles);
			}
		}
	}
}

// Called when the game starts or when spawned
void AFileDialog::BeginPlay()
{
	Super::BeginPlay();

}

// Called every frame
void AFileDialog::Tick(float DeltaTime)
{
	Super::Tick(DeltaTime);

}

