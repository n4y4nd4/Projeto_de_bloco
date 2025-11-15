# Script para configurar o Gradle Wrapper
# Baixa o gradle-wrapper.jar necessario

$wrapperDir = "gradle\wrapper"
$jarPath = "$wrapperDir\gradle-wrapper.jar"

# Cria o diretorio se nao existir
if (-not (Test-Path $wrapperDir)) {
    New-Item -ItemType Directory -Path $wrapperDir -Force | Out-Null
}

# URL do gradle-wrapper.jar (versao 8.5)
$jarUrl = "https://raw.githubusercontent.com/gradle/gradle/v8.5.0/gradle/wrapper/gradle-wrapper.jar"

Write-Host "Baixando gradle-wrapper.jar..."
try {
    Invoke-WebRequest -Uri $jarUrl -OutFile $jarPath -UseBasicParsing
    Write-Host "gradle-wrapper.jar baixado com sucesso!"
} catch {
    Write-Host "Erro ao baixar: $_"
    Write-Host "Tentando metodo alternativo..."
    
    # Metodo alternativo: usar o Gradle instalado
    if (Get-Command gradle -ErrorAction SilentlyContinue) {
        Write-Host "Usando Gradle instalado para gerar wrapper..."
        gradle wrapper --gradle-version 8.5
    } else {
        Write-Host "Por favor, instale o Gradle ou baixe manualmente o gradle-wrapper.jar"
        Write-Host "URL: $jarUrl"
        exit 1
    }
}

Write-Host "Gradle Wrapper configurado com sucesso!"
