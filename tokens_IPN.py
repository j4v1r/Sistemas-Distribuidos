import sys
import random
import string

if len(sys.argv) > 1:
    n = int(sys.argv[1])

else:
    print("SIN ARGUMENTOS")

cadenota=[]
for i in range(n):
	random_token=' '.join(random.choices(string.ascii_uppercase,k=3)) + ' '
	cadenota.append(random_token)

contador=0
for i,token in enumerate(cadenota,1):
	if token=='I P N ':
		contador+=1

print(contador)