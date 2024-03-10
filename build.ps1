Set-PSDebug -Trace 1
docker build -t telegram-build .
echo "{PWD} = ${PWD}"
docker run --rm -v "${PWD}:/home/source" telegram-build
Read-Host -Prompt "Press Enter to exit"