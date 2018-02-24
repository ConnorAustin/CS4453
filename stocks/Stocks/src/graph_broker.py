import sys
import matplotlib.pyplot as plt

data = []
try:
    with open(sys.argv[1]) as dataFile:
        for line in dataFile:
            data.append(line.split(','))
except:
    print('Could not read file')
    sys.exit(1)

header = data[0]
data = data[1:]

plt.figure(1)
plt.title(header[0])
plt.xlabel('Dates')
plt.ylabel('Closing Price')

x = []
y = []
skip = 1
for i in range(0, len(data), skip):
    l = data[i]
    x.append(i)
    y.append(float(l[1]))
lines = plt.plot(x, y)
buy_color = 'blue'
sell_color = 'green'

for i in range(0, len(data), skip):
    l = data[i]

    annotation_col = 'gray'
    if l[2] == 'SELL':
        annotation_col = sell_color
    elif l[2] == 'BUY':
        annotation_col = buy_color
    else:
        continue
    plt.annotate(
        l[2],
        xy=(i, float(l[1])), xytext=(-10, 10),
        textcoords='offset points', ha='right', va='bottom',
        bbox=dict(fc=annotation_col, alpha=0.5),
        arrowprops=dict(arrowstyle = '-', connectionstyle='arc3,rad=0'))

for line in lines:
    plt.setp(line, color='black', linewidth=2.0, label='Dick')

beginDate = data[0][0]
midDate = data[int(len(data)/2)][0]
endDate = data[len(data) - 1][0]

plt.xticks([0, len(data) / 2, len(data)], (beginDate, midDate, endDate))
plt.show()
