## Aqui se definira el script que se encargara de hacer deploy solo

server="hgrid5"
clients=("hgrid6" "hgrid7" "hgrid8" "hgrid9" "hgrid10" "hgrid11" "hgrid12" "hgrid13" "hgrid15")

scp ../server/build/libs/server.jar "swarch@$server:Documents/JFCandJCR/"


for client in "${clients[@]}"
do
    scp ../client/build/libs/client.jar "swarch@$client:Documents/JFCandJCR/"

done