import os
from functools import partial

import numpy as np


def calcular_pi(distance, ui0):
    uij = 1 / np.power(distance, 2)
    return uij / ui0


def to_file(I, J, file, demand, gamma, gammas, cost, allocationCosts, U0):
    name = f"../instances/ORlib/{file}-{gamma}.txt"
    print(f"Generating {name}")
    if not os.path.isdir(os.path.dirname(name)):
        os.makedirs(os.path.dirname(name), exist_ok=True)

    with open(name, "w") as f:
        f.write(f"I: {I}\n")
        f.write(f"J: {J}\n")
        f.writelines(["COSTS:\n"] + [f"{bp}\n" for bp in cost])
        f.write(f"U0: {U0}\n")
        f.write(f"GAMMA: {gamma}\n")
        f.writelines(["GAMMAS:\n"] + [f"{g}\n" for g in gammas])
        f.writelines(["BUYING POWER:\n"] + [f"{bp}\n" for bp in demand])

        f.write(f"PI:\n")
        for pi in allocationCosts:
            f.write(" ".join([f"{pia}" for pia in pi]))
            f.write(f"\n")


def readORLibFile(path: str, UI0):
    with open(path) as f:
        lines = list(map(lambda x: x.strip(), f.readlines()))
        splitLine = lines[0].split(" ")
        j = int(splitLine[0])
        i = int(splitLine[1])
        capacities = []
        costs = []
        index = 0
        for cc in range(1, j + 1):
            splitLine = lines[cc].split(" ")
            capacity = splitLine[0]
            cost = splitLine[1]
            capacities.append(None if capacity == "capacity" else float(capacity))
            costs.append(float(cost))

        index = j + 1
        demands = []
        allocationCosts = []
        for ii in range(0, i):
            demand = int(lines[index])
            demands.append(demand)
            index += 1
            allocationCostsParcial = []
            readedCosts = 0
            while readedCosts < j:
                splitLine = lines[index].split(" ")
                allocationCostsParcial += list(
                    map(partial(calcular_pi, ui0=UI0), map(lambda x: x / demand, map(float, splitLine))))
                index += 1;
                readedCosts += len(splitLine)
            allocationCosts.append(allocationCostsParcial)
        return i, j, capacities, costs, demands, allocationCosts


U0 = 1 / 1000000

if __name__ == '__main__':
    file_names = ["capa", "capb", "capc"]
    for file_name in file_names:
        i, j, capacities, costs, demands, allocationCosts = readORLibFile(file_name, U0)
        gammas = [1, 2, 3, "nh"]
        # O este valor viene de alguna parte rara como el allocationCosts
        for Gamma in gammas:
            if Gamma == "nh":
                # Non homogenous gamma
                np.random.seed(0)
                if i == 100 or i == 200:
                    # R1 R2
                    gammas = np.random.randint(1, 40, i)
                else:
                    # R3 ORLib
                    gammas = np.random.randint(1, 6, i)
            else:
                gammas = [Gamma for _ in range(i)]
            to_file(i, j, file_name, demands, Gamma, gammas, costs, allocationCosts, U0)
