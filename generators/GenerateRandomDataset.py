"""
@author: Y.H.Lin

This is the python file for generating the random dataset in 

    Lin, Y. H., & Tian, Q. (2020). Branch-and-cut Approach based on Generalized Benders Decomposition 
    for Facility Location with Limited Choice Rule. European Journal of Operational Research.

It uses the number of customers, the number of facilities, the random seed as inputs.

This script is addapter to facilitate the generation of the datasets for the paper.
"""

import os

import numpy as np


def distanceij(i_loc, j_loc):
    """
    Java code
        var x1 = this.facilities[j][0];
        var y1 = this.facilities[j][1];
        var x2 = this.customers[i][0];
        var y2 = this.customers[i][1];
        var minusX = x2 - x1;
        var minusY = y2 - y1;
        return Math.sqrt(minusY * minusY + minusX * minusX);

    :param i_loc:
    :param j_loc:
    :return:
    """
    minusx = i_loc[0] - j_loc[0]
    minusy = i_loc[1] - j_loc[1]

    minusx = np.power(minusx, 2)
    minusy = np.power(minusy, 2)
    return np.sqrt(minusx + minusy)


def calcular_pi(i_loc, j_loc, ui0):
    distance = distanceij(i_loc, j_loc)
    uij = 1 / np.power(distance, 2)
    return uij / ui0


def GenerateData(Number_of_customers, Number_of_facilities, random_seed, U0, cost):
    np.random.seed(random_seed)
    Cus_location, Facility_location = [], []

    Demand = np.random.randint(10, 1000, Number_of_customers)

    Location_Cus_x = np.random.uniform(0, 1000, Number_of_customers)
    Location_Cus_y = np.random.uniform(0, 1000, Number_of_customers)

    Facility_Location_x = np.random.uniform(0, 1000, Number_of_facilities)
    Facility_Location_y = np.random.uniform(0, 1000, Number_of_facilities)

    # Código de enrique
    # BuyingPower = np.random.uniform(10, 1000, Number_of_customers)

    for i in range(Number_of_customers):
        Cus_location.append((Location_Cus_x[i], Location_Cus_y[i]))

    for i in range(Number_of_facilities):
        Facility_location.append((Facility_Location_x[i], Facility_Location_y[i]))

    pi = np.zeros((Number_of_customers, Number_of_facilities))
    for i in range(Number_of_customers):
        for j in range(Number_of_facilities):
            pi[i][j] = calcular_pi(Cus_location[i], Facility_location[j], U0)

    costs = np.empty(Number_of_facilities)
    costs.fill(cost)

    return Cus_location, Facility_location, Demand, pi, costs


def to_file(prefix, I, J, seed, Customers, Facilities, Demand, Gamma, gammas, pis, cost, costs, U0):
    name = f"../instances/{prefix}/{I}-{J}-{cost}-{Gamma}-{seed}.txt"
    print(f"Generating {name}")
    if not os.path.isdir(os.path.dirname(name)):
        os.makedirs(os.path.dirname(name), exist_ok=True)

    with open(name, "w") as f:
        f.write(f"I: {I}\n")
        f.write(f"J: {J}\n")
        f.write(f"COST: {cost}\n")
        f.writelines(["COSTS:\n"] + [f"{scost}\n" for scost in costs])
        f.write(f"U0: {U0}\n")
        f.write(f"SEED: {seed}\n")
        f.write(f"GAMMA: {Gamma}\n")
        f.writelines(["GAMMAS:\n"] + [f"{g}\n" for g in gammas])
        f.writelines(["CUSTOMERS:\n"] + [f"{c[0]} {c[1]}\n" for c in Customers])
        f.writelines(["FACILITIES:\n"] + [f"{f[0]} {f[1]}\n" for f in Facilities])
        f.writelines(["BUYING POWER:\n"] + [f"{bp}\n" for bp in Demand])

        f.write(f"PI:\n")
        for pi in pis:
            f.write(" ".join([f"{pia}" for pia in pi]))
            f.write(f"\n")


def generate_files(gammas, costs, seeds, Is, Js, U0):
    for I, J, seed, cost, Gamma, U0 in generate_instances(gammas, costs, seeds, Is, Js, U0):
        Cus_location, Facility_location, Demand, pis, costs = GenerateData(I, J, seed, U0, cost)

        if Gamma == "nh":
            # Non homogenous gamma
            np.random.seed(0)
            if I == 100 or I == 200:
                # R1 R2
                gammas = np.random.randint(1, 40, I)
            else:
                # R3 S1 ORLib
                gammas = np.random.randint(1, 6, I)
        else:
            gammas = [Gamma for _ in range(I)]

        yield I, J, seed, Cus_location, Facility_location, Demand, Gamma, gammas, pis, cost, costs, U0


def generate_instances(gammas, costs, seeds, Is, Js, U0):
    for Gamma in gammas:
        for cost in costs:
            for seed in seeds:
                for I in Is:
                    for J in Js:
                        yield I, J, seed, cost, Gamma, U0


class configuration():
    def run(self):
        for I, J, seed, Cus_location, Facility_location, Demand, Gamma, gammas, pis, cost, costs, U0 in generate_files(
                self.Gammas, self.costs, self.seeds, self.Is, self.Js, self.U0):
            yield I, J, seed, Cus_location, Facility_location, Demand, Gamma, gammas, pis, cost, costs, U0


def to_file_r(prefix: str, r: configuration):
    for I, J, seed, Cus_location, Facility_location, Demand, Gamma, gammas, pis, cost, costs, U0 in r.run():
        to_file(prefix, I, J, seed, Cus_location, Facility_location, Demand, Gamma, gammas, pis, cost, costs, U0)


class r1(configuration):
    # R1
    Is = [100]
    Js = [100]
    costs = [500, 1000, 2000]
    U0 = 1 / (100 * 100)
    Gammas = [1, 2, 3, 4, 5, 7, 10, 20, "nh"]
    seeds = [1, 2, 3, 4, 5]


class r2(configuration):
    # R2
    Is = [200]
    Js = [100]
    costs = [500, 2000]
    U0 = 1 / (100 * 100)
    Gammas = [1, 2, 3, 4, 5, 7, 10, 20, "nh"]
    seeds = [1, 2, 3, 4, 5]


class r3(configuration):
    # R3
    Is = [800, 1000]
    Js = [100, 150, 200]
    costs = [2000]
    U0 = 1 / (50 * 50)
    Gammas = [1, 2, 3, "nh"]
    seeds = [1]


class s1(configuration):
    Is = [1500, 2000, 3000]
    Js = [300, 500]
    costs = [2000, 3000]
    U0 = 1 / (50 * 50)
    Gammas = [1, 2, 3, "nh"]
    seeds = [1]


if __name__ == "__main__":
    to_file_r("R1", r1())
    to_file_r("R2", r2())
    to_file_r("R3", r3())
    to_file_r("S1", s1())
