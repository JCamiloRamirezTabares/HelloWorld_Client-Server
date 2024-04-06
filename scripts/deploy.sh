## Aqui se definira el script que se encargara de hacer deploy solo

server="hgrid5"
clients=("hgrid6" "hgrid7" "hgrid8")

scp ../server/build/libs/server.jar "swarch@$server:Documents/JFCandJCR/"


for client in "${clients[@]}"
do
    scp ../client/build/libs/client.jar "swarch@$client:Documents/JFCandJCR/"

done