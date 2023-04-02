import plotly.graph_objects as go
import argparse
import os
import random
import pandas as pd
import plotly.express as px

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

def get_ops(lineNum, folder):
    if os.path.exists(folder):
        flist = generate_files_list(folder)
        flist = sorted(flist)
        dataAux = []
        for file in flist:
            f = open(file, "r")
            list = f.readlines()
            line = list[lineNum]
            op = float(line.split(": ")[1])
            dataAux.append(op)
        return sum(dataAux)/len(dataAux)

def plot_data1():
    unassign = [get_ops(2, "outputs/exp1")]
    moves = [get_ops(1, "outputs/exp1")]
    swaps = [get_ops(0, "outputs/exp1")]

    langs = ['Primera heurística']

    fig = go.Figure()


    fig.add_trace(go.Bar(
        x=langs,
        y=unassign,
        name='Unassign',
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
    fig.update_layout(barmode='group', xaxis_tickangle=-45, title_text="Accions")
    fig.show()
    fig.write_image("plots/exp1.png")
    
def plot_data2():
    time = [get_ops(1, "outputs/exp2/sol" + str(i)) for i in range(2)]
    dist = [get_ops(2, "outputs/exp2/sol" + str(i)) for i in range(2)]
    drivers = [get_ops(3, "outputs/exp2/sol" + str(i)) for i in range(2)]

    langs = ['Solució inicial 0', 'Solució inicial 1']

    fig = go.Figure()

    fig.add_trace(go.Bar(
        x=langs,
        y=time,
        name='Time',
        marker_color='lightsalmon'
    ))

    fig.add_trace(go.Bar(
        x=langs,
        y=dist,
        name='Distance',
        marker_color='lightslategray'
    ))

    fig.add_trace(go.Bar(
        x=langs,
        y=drivers,
        name='Drivers',
        marker_color='rgb(69,117,180)'
    ))

    # Here we modify the tickangle of the xaxis, resulting in rotated labels.
    fig.update_layout(barmode='group', xaxis_tickangle=-45, title_text="Resultat")
    fig.show()
    fig.write_image("plots/exp2.png")


def get_distance(path):
    file = open(path, 'r')
    lines = file.readlines()
    line = lines[2]
    return float(line.split(": ")[1])

def get_time(path):
    file = open(path, 'r')
    lines = file.readlines()
    line = lines[1]
    return float(line.split(": ")[1])


def plot_data3():
    flist = generate_files_list("outputs/exp3")
    flist = sorted(flist)
    dictRes = {}
    lst = []
    title = None
    for file in flist:
        newTitle = file.split('/')[-1].split('-')
        if len(newTitle) == 3:
            newTitle = newTitle[0] + '-' + newTitle[1]
        else:
            newTitle = newTitle[0]
        dst = get_distance(file)
        if title != None and title != newTitle:
            dictRes[title] = lst
            lst = []

        lst.append(dst)
        title = newTitle
    dictRes[title] = lst

    df = pd.DataFrame.from_dict(dictRes)
    # print(df.mean())
    idMax = 0
    idMin = 0
    means = df.mean()
    for i in range(len(means)):
        if means[i] > means[idMax]:
            idMax = i
        if means[i] < means[idMin]:
            idMin = i

    fig = px.line(df.iloc[:, [idMin, idMax]])
    fig.show()
    fig.write_image('plots/exp3_minmax.png')
    fig = px.line(df)
    fig.show()
    fig.write_image("plots/exp3.png")
    
def plot_data4(r):
    time = [get_ops(1, "outputs/exp4/" + str(i)) for i in r]

    langs = [str(i) for i in r]

    fig = go.Figure()

    fig.add_trace(go.Bar(
        x=langs,
        y=time,
        name='Time',
        marker_color='lightsalmon'
    ))

    # Here we modify the tickangle of the xaxis, resulting in rotated labels.
    fig.update_layout(barmode='group', xaxis_tickangle=-45, title_text="Temps")
    fig.show()
    fig.write_image("plots/exp4.png")
    
def plot_data5():
    time = [get_ops(1, "outputs/exp5/" + str(i)) for i in range(2)]
    dist = [get_ops(2, "outputs/exp5/" + str(i)) for i in range(2)]
    drivers = [get_ops(3, "outputs/exp5/" + str(i)) for i in range(2)]

    langs = ['Heurística 0', 'Heurística 1']

    fig = go.Figure()

    fig.add_trace(go.Bar(
        x=langs,
        y=time,
        name='Time',
        marker_color='lightsalmon'
    ))

    fig.add_trace(go.Bar(
        x=langs,
        y=dist,
        name='Distance',
        marker_color='lightslategray'
    ))

    fig.add_trace(go.Bar(
        x=langs,
        y=drivers,
        name='Drivers',
        marker_color='rgb(69,117,180)'
    ))

    # Here we modify the tickangle of the xaxis, resulting in rotated labels.
    fig.update_layout(barmode='group', xaxis_tickangle=-45, title_text="Resultat")
    fig.show()
    fig.write_image("plots/exp5-2.png")

    
def plot_data6():
    time = [get_ops(1, "outputs/exp6/" + str(i)) for i in range(2)]
    dist = [get_ops(2, "outputs/exp6/" + str(i)) for i in range(2)]

    langs = ['Heurística 0', 'Heurística 1']

    fig = go.Figure()

    fig.add_trace(go.Bar(
        x=langs,
        y=time,
        name='Time',
        marker_color='lightsalmon'
    ))

    fig.add_trace(go.Bar(
        x=langs,
        y=dist,
        name='Distance',
        marker_color='lightslategray'
    ))

    # Here we modify the tickangle of the xaxis, resulting in rotated labels.
    fig.update_layout(barmode='group', xaxis_tickangle=-45, title_text="Resultat")
    fig.show()
    fig.write_image("plots/exp6.png")

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
    heur = 0
    for n in range(10):
        seed = random.randint(0, 2**31 - 1)
        outputFile = "outputs/exp1/" + str(n) + ".txt"
        execute(path, 200, 100, seed, heur, 0, 0, 0, 0, 0, 0, 1, 0, 0, outputFile)
        
def execute_experiment2(path):
    heur = 0
    for n in range(10):
        seed = random.randint(0, 2**31 - 1)
        for i in range(2):
            outputFile = "outputs/exp2/sol" + str(i) + "/" + str(n) + ".txt"
            execute(path, 200, 100, seed, heur, i, 0, 0, 0, 0, 0, 0, 1, 0, outputFile)
            
def execute_experiment3(path):
    ks = [5**x for x in range(6)]
    lambdas = list(map(lambda x: 1.0 / (10 ** x), range(10)))
    stiters = [x*10 for x in range(1, 10)]
    heur = 0
    solIni = 1
    steps = 1000
    for n in range(10):
        seed = random.randint(0, 2**31 - 1)
        for k in ks:
            for lambd in lambdas:
                for stiter in stiters:
                    outputFile = "outputs/exp3/" + str(stiter) + "_" + str(k) + "_" + str(lambd) + "-" + str(n) + ".txt"
                    execute(path, 50, 25, seed, heur, solIni, 1, steps, stiter, k, lambd, 0, 1, 0, outputFile)
                    
def execute_experiment4(path, r):
    heur = 0
    solIni = 1
    for n in r:
        create_folder(r"outputs/exp4/" + str(n))
        for i in range(10):
            seed = random.randint(0, 2**31 - 1)
            outputFile = "outputs/exp4/" + str(n) + "/" + str(i) + ".txt"
            execute(path, n, n // 2, seed, heur, solIni, 0, 0, 0, 0, 0, 0, 1, 0, outputFile)
            
def execute_experiment5(path):
    solIni = 1
    for h in range(2):
        create_folder(r"outputs/exp5/" + str(h))
        for i in range(10):
            seed = random.randint(0, 2**31 - 1)
            outputFile = "outputs/exp5/" + str(h) + "/" + str(i) + ".txt"
            execute(path, 200, 100, seed, h, solIni, 0, 0, 0, 0, 0, 0, 1, 0, outputFile)


def execute_experiment6(path):
    # parameters obtained in experiment 3
    solIni = 1
    steps = 1000
    stiter = 60
    k = 1
    lambd = 0.00001
    for h in range(2):
        create_folder(r"outputs/exp6/" + str(h))
        for i in range(10):
            seed = random.randint(0, 2**31 - 1)
            outputFile = "outputs/exp6/" + str(h) + "/" + str(i) + ".txt"
            execute(path, 200, 100, seed, h, solIni, 1, steps, stiter, k, lambd, 0, 1, 0, outputFile)


def create_folder(folder_name):
    current_directory = os.getcwd()
    final_directory = os.path.join(current_directory, folder_name)
    if not os.path.exists(final_directory):
        os.makedirs(final_directory)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('path', help='Path of .jar file')
    parser.add_argument('experiment', help='Experiment to execute (0 for all)')
    args = parser.parse_args()
    
    exp = int(args.experiment)
    create_folder(r"plots")
    
    if exp == 1 or exp == 0:
        # experiment 1
        create_folder(r"outputs/exp1")
        execute_experiment1(args.path)
        plot_data1()
    
    if exp == 2 or exp == 0:
        # experiment 2
        create_folder(r"outputs/exp2/sol0")
        create_folder(r"outputs/exp2/sol1")
        execute_experiment2(args.path)
        plot_data2()
        
    if exp == 3 or exp == 0:
        # experiment 3
        create_folder(r"outputs/exp3")
        execute_experiment3(args.path)
        plot_data3()
        
    if exp == 4 or exp == 0:
        # experiment 4
        r = range(200, 600, 100)
        create_folder(r"outputs/exp4")
        #execute_experiment4(args.path, r)
        plot_data4(r)

    if exp == 5 or exp == 0:
        # experiment 5
        create_folder(r"outputs/exp5")
        #execute_experiment5(args.path)
        plot_data5()
        
    if exp == 6 or exp == 0:
        # experiment 6
        create_folder(r"outputs/exp6")
        execute_experiment6(args.path)
        plot_data6()
        
    if exp == 7 or exp == 0:
        # experiment 7
        create_folder(r"outputs/exp7")
        execute_experiment7(args.path)
        plot_data7()
        
