{ nixpkgs ? import <nixpkgs> { },  beRoot }:
with nixpkgs;
stdenv.mkDerivation rec {
  name = "shapeless-backend";
  src = beRoot;
  buildInputs = [ adoptopenjdk-jre-bin gradle ];
  phases = [ "unpackPhase" "buildPhase" "installPhase" ];
  unpackPhase = ''
    mkdir -p ./Shapeless
    cp -R $src/* ./Shapeless
  '';
  buildPhase = ''
    cd Shapeless
    gradle :designer:clean
    gradle :dispatcher:clean
    gradle :designer:bootJar
    gradle :dispatcher:bootJar
  '';
  installPhase = ''
    mkdir -p $out/dispatcher $out/designer
    cp environments/dev/backend/dockerfiles/docker-entrypoint/docker-entrypoint.sh $out/dispatcher/docker-entrypoint.sh
    cp environments/dev/backend/dockerfiles/docker-entrypoint/docker-entrypoint.sh $out/designer/docker-entrypoint.sh
    cp dispatcher/build/libs/dispatcher.jar $out/dispatcher/dispatcher.jar
    cp designer/build/libs/designer.jar $out/designer/designer.jar
  '';
}
