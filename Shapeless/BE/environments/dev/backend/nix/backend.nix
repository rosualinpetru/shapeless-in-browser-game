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
    gradle :dispatcher:clean
    gradle :dispatcher:app:bootJar
  '';
  installPhase = ''
    mkdir -p $out/dispatcher
    cp environments/dev/backend/dockerfiles/docker-entrypoint/docker-entrypoint.sh $out/dispatcher/docker-entrypoint.sh
    cp dispatcher/app/build/libs/dispatcher.jar $out/dispatcher/dispatcher.jar
  '';
}
