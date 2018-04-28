library(caret)
library(nnet)
library(class)

fix_sex <- function(sex) {
    if(sex == 'male') {
        return(1)
    }
    else {
        return(0)
    }
}

fix_age <- function(age) {
    if(is.na(age)) {
        return(0)
    } else {
        return(age)
    }
}

clean_data <- function(data) {
    # Drop low-priority columns
    data <- subset(data, select = -PassengerId)
    data <- subset(data, select = -Name)
    data <- subset(data, select = -Ticket)
    data <- subset(data, select = -Cabin)
    data <- subset(data, select = -Embarked)

    data['Sex'] <- apply(data['Sex'], 1, fix_sex)
    data['Age'] <- apply(data['Age'], 1, fix_age)
    
    return(data)
}

data <- read.csv(file='tit-train.csv', head=TRUE, sep=",")
data <- clean_data(data)

train = sample(1:149,100)
test = setdiff(1:149, train)

eval_data <- read.csv(file='tit-test.csv', head=TRUE, sep=",")
eval_data <- clean_data(eval_data)

grid <- expand.grid(size=c(6,7,8,9,10,11,12,13,14,15), decay=c(0,0.01,0.1,1))
nfit <- train(Survived~Pclass+Sex+Age+SibSp+Parch+Fare, data=data[train,], method="nnet", tuneGrid=grid,skip=FALSE,linout=FALSE)

testpred <- predict(nfit, subset(data[test,], select = -Survived), type="raw")
testpred <- apply(testpred, 1, round)

pred <- predict(nfit, eval_data, type="raw")
pred <- apply(pred, 1, round)

misclass <- 0

for (i in 1 : length(testpred)) {
    if(testpred[i] != data[test,'Survived'][i]) {
        misclass <- misclass + 1
    }
}
print('Accuracy (%):')
print(100 - 100 * misclass / length(testpred))

print('')

survived <- sum(pred)
died <- length(pred) - survived

print('tit-test.csv total survived:')
print(survived)

print('')

print('tit-test.csv total died:')
print(died)

print('')

print('tit-test.csv values:')
print(pred)
