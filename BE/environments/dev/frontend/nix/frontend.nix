{ nixpkgs ? import <nixpkgs> { }, feRoot, nginxConf, version }:
with nixpkgs;
stdenv.mkDerivation rec {
  pname = "shapeless-frontend";
  inherit version;
  phases = [ "installPhase" ];
  installPhase = ''
    mkdir $out
    mkdir $out/app
    mkdir $out/nginx
    shopt -s dotglob
    shopt -s extglob
    cp -R ${feRoot}/!(node_modules) $out/app
    rm -rf $out/app/node_modules
    cp ${nginxConf} $out/nginx/nginx.conf
  '';
}
