from scipy.sparse import random
from scipy import stats
import numpy as np

class CustomRandomState(np.random.RandomState):
     def randint(self, k):
         i = np.random.randint(k)
         return i - i % 2

np.random.seed(12345)
rs = CustomRandomState()
rvs = stats.poisson(25, loc=10).rvs
S = random(5, 5, density=0.25, random_state=rs, data_rvs=rvs)
R = random(5, 5, density=0.25, random_state=rs, data_rvs=rvs)

res = (S * R).tocoo()

with open("Matrix1.txt", "w") as text_file:
    for x in range(S.row.size):
        text_file.write(str(S.row[x]) + ',' + str(S.col[x]) + ',' + str(S.data[x]) + "\n")


with open("Matrix2.txt", "w") as text_file:
    for x in range(R.row.size):
        text_file.write(str(R.row[x]) + ',' + str(R.col[x]) + ',' + str(R.data[x]) + "\n")

with open("Result.txt", "w") as text_file:
    for x in range(res.row.size):
        text_file.write(str(res.row[x]) + ',' + str(res.col[x]) + ',' + str(res.data[x]) + "\n")

