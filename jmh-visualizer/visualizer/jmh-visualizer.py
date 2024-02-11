import json
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
# import mplcursors

with open('C:/Users/lucas/Desktop/GitHub/java-data-structures'
          '/java-data-structures/benchmarks/jmh-results_2.json', 'r') as f:
    jmh_results = json.load(f)


def read_results_structure():
    current_chart = ""
    current_benchmark = ""
    charts = 0
    benchmarks = []
    figures_titles = []
    for entry in jmh_results:
        benchmark_class = entry['benchmark'].split('.')[-2]
        if benchmark_class != current_chart:
            current_chart = benchmark_class
            charts += 1
            figures_titles.append(current_chart)
            benchmarks.append(1)
            current_benchmark = entry['benchmark'].split('.')[-1]
        else:
            benchmark = entry['benchmark'].split('.')[-1]
            if benchmark == current_benchmark:
                continue
            else:
                current_benchmark = benchmark
                benchmarks[charts - 1] += 1

    return benchmarks, figures_titles


def collect_data():
    current_benchmark = jmh_results[0]['benchmark'].split('.')[-1]
    j = 0
    i = 0
    scores = []
    cis = []
    parameter_values = []
    score_unit = ""
    for entry in jmh_results:
        benchmark = entry['benchmark'].split('.')[-1]
        if current_benchmark != benchmark:
            df = pd.DataFrame({'N': parameter_values, 'avg_time': scores, 'ci_values': cis})
            if benchmarks[i] <= 3:
                chart(current_benchmark, score_unit, df, tot_axes[i][j])
            else:
                q, r = divmod(j, 3)
                chart(current_benchmark, score_unit, df, tot_axes[i][q][r])
            current_benchmark = benchmark
            scores = []
            cis = []
            parameter_values = []
            j += 1
            if j >= benchmarks[i]:
                i += 1
                j = 0
        scores.append(entry['primaryMetric']['score'])
        parameter_values.append(entry['params']['N'])
        score_unit = entry['primaryMetric']['scoreUnit']
        cis.append((entry['primaryMetric']['scoreConfidence'][0], entry['primaryMetric']['scoreConfidence'][1]))

    q, r = divmod(j, 3)
    df = pd.DataFrame({'N': parameter_values, 'avg_time': scores, 'ci_values': cis})
    chart(current_benchmark, score_unit, df, tot_axes[i][q][r])


benchmarks, titles = read_results_structure()
tot_axes = []
with sns.axes_style('darkgrid'):
    for chart in range(len(benchmarks)):
        cols = 3
        quotient, remainder = divmod(benchmarks[chart], 3)
        if quotient == 0:
            cols = benchmarks[chart]
            remainder = 1
        figure, axes = plt.subplots(quotient + remainder, cols,
                                    figsize=(20, 12), dpi=80, sharex=True)
        figure.suptitle(titles[chart])
        sns.despine()
        tot_axes.append(axes)


def chart(title, score_unit, data, ax):
    data['N'] = pd.to_numeric(data['N'], errors='coerce')
    # distances = [0] + [abs(data['N'].iloc[i] - data['N'].iloc[i-1]) for i in range(1, len(data['N']))]
    # positions = [sum(distances[: i+1]) for i in range(len(distances))]
    sns_plot = sns.lineplot(x="N", y="avg_time", data=data, ax=ax, marker='o')
    ci_low, ci_high = zip(*data['ci_values'])
    ci_low = tuple(value if value >= 0 else 0 for value in ci_low)
    ax.fill_between(data['N'], ci_low, ci_high, color='skyblue', alpha=0.3)
    ax.set_xticks(data['N'], labels=data['N'])
    ax.set_title(title + ' [' + score_unit + ']')
    sns_plot.set_xticklabels(sns_plot.get_xticklabels(), rotation=45, fontsize=9)


collect_data()
# cursor = mplcursors.cursor(hover=True)
# plt.tight_layout()
plt.show()
