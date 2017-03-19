import numpy as np
import matplotlib.pyplot as plt
from sklearn.ensemble import IsolationForest


def load_x_train () :
    filename_read='C:/Chamini/data/feactures_auth_1.txt'
    list_data=[[]]
    j=0
    with open(filename_read) as f:
        for line in f:
            array_split=line.split(',')
            array_split.pop(0)
            array_split.pop()
            
            #print('J: ' + str (j))
            
            for i in array_split:
                #print(float(i))
                list_data[j].append(float(i))
            list_data.append([])
            j=j+1
    
    list_data.pop()
    return list_data
    #print(list_data)        
    #implement_cluster(list_data)

def load_x_test () :
    filename_read='C:/Chamini/data/feactures_auth_2.txt'
    list_data=[[]]
    j=0
    with open(filename_read) as f:
        for line in f:
            array_split=line.split(',')
            array_split.pop(0)
            array_split.pop()
            #print(array_split)
            #print('J: ' + str (j))
            
            for i in array_split:
                #print(float(i))
                list_data[j].append(float(i))
            list_data.append([])
            j=j+1
    
    list_data.pop()
    return list_data


    
def load_x_outliner () :
    filename_read='C:/Chamini/data/feactures_readteam.txt'
    list_data=[[]]
    j=0
    with open(filename_read) as f:
        for line in f:
            array_split=line.split(',')
            array_split.pop(0)
            array_split.pop()
            #print(array_split)
            #print('J: ' + str (j))
            
            for i in array_split:
                #print(float(i))
                list_data[j].append(float(i))
            list_data.append([])
            j=j+1
    
    list_data.pop()
    return list_data
    
    
rng = None

# Generate train data
#X = 0.3 * rng.randn(100, 2)
#X_train = np.r_[X + 2, X - 2]
X_train=load_x_train()
# Generate some regular novel observations
#X = 0.3 * rng.randn(20, 2)
#X_test = np.r_[X + 2, X - 2]
X_test=load_x_test()
# Generate some abnormal novel observations
#X_outliers = rng.uniform(low=-4, high=4, size=(20, 2))
X_outliers=load_x_outliner()


# fit the model
clf = IsolationForest(max_samples=100, random_state=rng)
clf.fit(X_train)
y_pred_train = clf.predict(X_train)
y_pred_test = clf.predict(X_test)
y_pred_outliers = clf.predict(X_outliers)

i_cnt=0
for i in y_pred_test:
    if i == 1:
        i_cnt=i_cnt+1 

print('i_cnt: '+ str(i_cnt))
print(y_pred_outliers)

