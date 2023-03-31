import plotly.graph_objects as go
import argparse
import os
import random

def generate_files_list(path):
    if path[-1] == '/':
        path = path[:-1]

    lfiles = []

    for lf in os.walk(path):
        if lf[2]:
            for f in lf[2]:
                if f[0] != '.':
                    lfiles.append(lf[0] + '/' + f)
    return lfiles

def get_ops(lineNum):
    ops = []
    iniSol = 0
    if os.path.exists("outputs"):
        flist = generate_files_list("outputs")
        flist = sorted(flist)
        dataAux = []
        for file in flist:
            f = open(file, "r")
            list = f.readlines()
            line = list[lineNum]
            op = float(line.split(": ")[1])
            dataAux.append(op)
        ops.append(sum(dataAux)/len(dataAux))
    return ops

def plot_data():
    unassign = get_ops(-1)
    moves = get_ops(-2)
    swaps = get_ops(-3)

    langs = ['h0']

    fig = go.Figure()


    fig.add_trace(go.Bar(
        x=langs,
        y=unassign,
        name='Remove',
        marker_color='lightsalmon'
    ))

    fig.add_trace(go.Bar(
        x=langs,
        y=moves,
        name='Move',
        marker_color='lightslategray'
    ))

    fig.add_trace(go.Bar(
        x=langs,
        y=swaps,
        name='Swap',
        marker_color='rgb(69,117,180)'
    ))

    # Here we modify the tickangle of the xaxis, resulting in rotated labels.
    fig.update_layout(barmode='group', xaxis_tickangle=-45, title_text="Acciones")
    fig.show()
    fig.write_image("plots/expansions.png")

def execute(path, nusuaris, ndrivers, seed, heuristica, solIni,
    hill_annealing, steps, stiter, k, lambd,
    imprimirAccions, imprimirRes, imprimirIni, outputFile):
    optNUsuaris = " " + str(nusuaris) + " "
    optNDrivers = " " + str(ndrivers) + " "
    optSeed = " " + str(seed) + " "
    optHeuristica = " " + str(heuristica) + " "
    optSolIni = " " + str(solIni) + " "
    optHillAnnealing = " " + str(hill_annealing) + " "
    optSteps = " " + str(steps) + " "
    optStiter = " " + str(stiter) + " "
    optK = " " + str(k) + " "
    optLambda = " " + str(lambd) + " "
    optImprimirAccions = " " + str(imprimirAccions) + " "
    optImprimirRes = " " + str(imprimirRes) + " "
    optImprimirIni = " " + str(imprimirIni) + " "
    opts = optNUsuaris + optNDrivers + optSeed + optHeuristica + optSolIni + optHillAnnealing + optSteps + optStiter + optK + optLambda + optImprimirAccions + optImprimirRes + optImprimirIni
    command = "java -jar -Xmx10000m " + "\"" + path + "\" " + opts + "> " + outputFile
    print(command)
    os.system(command)

def execute_experiment1(path):
    for n in range(10):
        seed = random.randint(0, 2**31 - 1)
        outputFile = "outputs/0_0_" + str(n) + ".txt"
        execute(path, 200, 100, seed, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, outputFile)

def create_folder(folder_name):
    current_directory = os.getcwd()
    final_directory = os.path.join(current_directory, folder_name)
    if not os.path.exists(final_directory):
        os.makedirs(final_directory)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('path', help='Path of .jar file')
    args = parser.parse_args()

    create_folder(r"outputs")
    execute_experiment1(args.path)

    create_folder(r"plots")
    plot_data()
