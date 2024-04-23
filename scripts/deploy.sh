## Aqui se definira el script que se encargara de hacer deploy solo

server="xhgrid5"
clients=("xhgrid6" "xhgrid7")



scp ../server/build/libs/server.jar "swarch@$server:Documents/JFCandJCR/"


for client in "${clients[@]}"
do
    scp ../client/build/libs/client.jar "swarch@$client:Documents/JFCandJCR/"

done