import os

path = os.getcwd();
for file in os.listdir(path):
    clean = file.replace("-","_").lower()
    os.rename(file,clean)
    