{ nixpkgs ? import <nixpkgs> { }, feRoot, nginxConf }:
with nixpkgs;
stdenv.mkDerivation rec {
  name = "shapeless-frontend";
  phases = [ "installPhase" ];
  installPhase = ''
    mkdir $out
    mkdir $out/app
    mkdir $out/nginx
    shopt -s dotglob
    cp -R ${feRoot}/* $out/app
    cp ${nginxConf} $out/nginx/nginx.conf
  '';
}
