## 파이썬 기본 문법 정리

## 목차

* [1. split() map()](#한-번에-여러개의-값을-입력-방는-방법)
* [2. ord() char()](#ord()와-char())
* [3. sys.stdin.readline()](#sys.stdin.readline())
* [4. sort(key = len)](#문자열-길이-순-정렬)
* [5. sort(key = lamda)](#sort()에서-key-lambda-사용하기)
* [6. Collections.Counter()](#Collections.Counter())
* [7. set()](#리스트의-중복을-제거하는-방법)
* [8. combinations()](#combinations())
* [9. count()](#count())
* [10. math.gcd()](#최대공약수)

</br>

## 한 번에 여러개의 값을 입력 방는 방법

```python
a, b = input().split() #string으로 반환
a, b = map(int, input()).split() #정수형으로 변환
a, b = map(float, input()).split() #실수형으로 변환
numList = list(map(int, input().split())) #1차원 배열 입력
```

## split()
#### : 문자열을 일정한 규칙으로 잘라서 리스트로 만들어 주는 함수

* ```문자열.split()```
* ```문자열.split('구분자')```
* ```문자열.split('구분자', 분할횟수)```
* ```문자열.split(sep='구분자', maxsplit=분할횟수)```

👉 문자열을 **sep을 기준**으로 **maxsplit만큼** 문자열을 잘라서 리스트로 만들어 줌

> ```sep 파라미터```
* 해당 파라미터의 기본값은 none -> 띄어쓰기, 엔터를 구분자로 하여 문자열을 자름
* 문자열.split(sep=',') 이라 한다면 문자열에서 "," 를 기준으로 자르게 됨
*  sep은 생략하고 문자열.split(',')으로 사용가능

> ```maxsplit```
* 기본값은 -1이며, 제한없이 자를 수 있을 때 까지 문자열 전체를 자름
* 문자열.split(maxsplit= 숫자)라면 문자열을 숫자만큼 자름
* maxsplit 를 생략이 가능하나 앞에 sep 파라미터가 있어야지만 가능

## map()

#### : 두 번째 인자로 들어온 반복 가능한 자료형을 첫 번째 인자로 들어온 함수에 하나씩 집어넣어서 함수를 수행

👉 ```map(function, iterable)```
* map 함수의 반환 값은 ```map객체```
* 해당 자료형을 list 혹은 tuple로 형 변환시키는 작업 필요!!

``` python
# 리스트값 + 1 을 수행해보자 

# 방법 1
numList = [1, 2, 3, 4, 5] 
result1 = [] 

for num in numList: 
    result1.append(num + 1) 
print(result)

# 방법 2
def add_one(n): 
    return n + 1 
result2 = list(map(add_one, numList)) 
print(result2)
```
* 방법 1은 for문으로 list에 append 해줘야하는 번거로움이 있음
* 하지만 방법2는 함수와 반복가능한 자료형만 넘겨주면 알아서 함수를 수행해줌!
* 반드시 원하는 자료형 (리스트, 튜플)으로 변환해주자!


## ord()와 char()

### ord()

하나의 ```문자```를 인자로 받고 해당 문자에 해당하는 ```아스키코드```를 반환
* ord('a')를 넣으면 정수 97을 반환
* 97(a) ~122(z)
* 65(A)~90(Z)

### char()

하나의 ```아스키코드```를 인자로 받고 해당 정수에 해당하는 ```문자```를 반환
* char(65) : A

## sys.stdin.readline()

```반복문으로 여러줄을 입력 받아야 할 때```는 input()으로 입력 받는다면 시간초과가 발생할 가능성이 높음!!

sys.stdin.readline()은 한줄 단위로 입력받기 때문에, 개행문자 '\n'이 입력되므로 형변환을 꼭 해주자!

#### 1. 한 개의 정수만 입력받기

```python
import sys
n = int(sys.stdin.readline())
```

#### 2. 한 줄에 여러개의 정수를 입력 받기

```python
import sys

n = int(input()) #테스트 케이스를 입력받는 경우 input() 사용 가능

for i in range(n):
        a,b = map(int, sys.stdin.readline().split())
        print(a+b)
```

## 문자열 길이 순 정렬

문자열을 길이 순으로 정렬해야할 때 key = len 옵션을 주자

```python
list.sort(key = len)
```

만약 알파벳 순으로 정렬하고 싶다면 sort()만 쓰면 됨!!

```python
list.sort()
```

## sort()에서 key lambda 사용하기

다음과 같은 리스트가 있다고 하자!

``` a = [(1, 2), (0, 1), (5, 1), (5, 2), (3, 0)]```

### 1. a.sort(key = lambda x: x[0])

```python
a = [(0, 1), (1, 2), (3, 0), (5, 1), (5, 2)]
```

→  첫번째 인자를 기준으로 오름차순으로 정렬됨

### 2. a.sort(key = lambda x : x[1])

```python
a = [(3, 0), (0, 1), (5, 1), (1, 2), (5, 2)]
```

→  두번째 인자를 기준으로 오름차순으로 정렬됨

### 3. a.sort(key = lambda x : x[0], x[1])

```python
a = [(0, 1), (1, 2), (3, 0), (5, 1), (5, 2)]
```
→  첫번 째 인자를 기준으로 오름 차순으로 정렬 후, 그리고 그 안에서 두 번째 인자를 기준으로 오름차순으로 정렬

## Collections.Counter()

파이썬에서 최빈값(mode)을 구하기 위해서는 collections 모듈의 Counter 라이브러리를 사용하자!

### 1. 선언

```python
from collections import Counter
```

### 2. 사용법

Counter 클래스의 **most_common()**  : 등장한 횟수를 내림차순으로 정리

```python
lst = []
for _ in range(n):
    lst.append(int(sys.stdin.readline())) # 리스트에 여러 개의 수를 입력함

count = Counter(lst).most_common()  # 내림차순으로 정렬 (by value)
```

* 만약 1 3 8 -2 2를 입력했다면 결과는 ```[(1, 1), (3, 1), (8, 1), (-2, 1), (2, 1)]```
* -1, -2, -3, -1, -2를 입력했다면 ```[(-1, 2), (-2, 2), (-3, 1)]```
* lst.sort() 후에 -1, -2. -3. -1. -2 를 입력했다면 ```[(-2, 2), (-1, 2), (-3, 1)]``` 
* value값으로 먼저 내림차순 정렬 후, value값이 동일하다면 key값으로 오름차순으로 정렬

### 3. [백준 문제 : 통계학](https://www.acmicpc.net/problem/2108)

카운터를 이용해서 구한 값이 [(-2, 2), (-1, 2), (-3, 1)] 라고 가정하면?
* ```lst[0][0]``` : -2
* ```lst[0][1]``` : 2
* ```lst[1][0]``` : -1
* ```lst[1][1]``` : 2

## 리스트의 중복을 제거하는 방법

### 1. set()

set은 **중복이 불가능**하다는 성질을 이용해서 리스트의 중복을 제거해볼 것임!

```python
list = [1, 2, 3 , 7, 1, 2, 1, 3]
result1 = set(list)
result2 = list(result1) # 다시 리스트로 변환하기

print(result1) # {1, 2, 3, 7} 
print(result2) # [1, 2, 3, 7]
```

### 2. 반복문

기존 리스트에 없는 값일 경우 새로운 리스트에 넣어주는 작업을 거치자!

```python
list = [1, 2, 3, 7, 1, 2, 1, 2]

new_list = []
for lst in list:
  if lst not in list:
    new_list.append(lst)
print(new_list)
```

## combinations()

### 1. 조합

* 어떠한 리스트에서 N개를 ```선택```할 수 있는 방법의 수를 의미함  
* 중복을 허용하지 않으며, 순서도 중요하지 않음. 그저 선택만 하면 됨!

### 2. 순열과의 차이 ?  
* 숫자가 적혀있는 공 5개 중 3개를 뽑는다고 가정하면 순열은 서로 다른 공 중 임의로 3개를 뽑아 순서대로 나열하는 것
* 조합은 3개를 뽑고 번호를 확인하지 않는 것 (그냥 뽑기만 하는 것!)


### 3. 사용법

```python
from itertools import combinations

nums = [1,2,3,4]
print(list(combinations(nums, 2))) # [(1, 2), (1, 3), (1, 4), (2, 3), (2, 4), (3, 4)]
print(list(combinations(nums, 3))) # [(1, 2, 3), (1, 2, 4), (1, 3, 4), (2, 3, 4)]
```       

## count()

```python
a = 'HelloWorld'
print(a.count('l')) # 3
print(a.count('l', 1, 4)) # 2
print(a.count('h')) # 0
```
* ```문자열.count('찾고자하는 문자')```의 형식을 통해서 문자의 개수를 알아낼 수 있음
* ```문자열.count('찾고자하는 문자', start, end)```의 형식을 사용하면 start ~ end -1 인덱스 사이에서 문자의 개수를 리턴함
* 대소문자를 구분함 → H가 아닌 h를 찾고자 하면 0이 리턴 
* 문자열과 리스트에서 사용 가능하나, dictionary, set자료형에서는 count 함수를 사용할 수 없음

## 최대공약수

최대 공약수(gcd)란 ```두 수를 나누는 수 중 가장 큰 수```를 의미한다. 

### math.gcd() 이용

```python
from math import gcd
a = gcd(72,60)
```
### for문 이용

```python
def gcd2(a, b): 
    if a > b:
        small = b
    else:
        small = a
    for i in range(1, small+1):
        if((a % i == 0) and (b % i == 0)):
            gcd = i              
    return gcd
```

### 재귀 이용

```python
def gcd1(x, y):
    if(y==0):
        return x
    else:
        return gcd1(y,x%y)
```

### 👉 [참고자료](https://www.delftstack.com/ko/howto/python/gcd-python/#:~:text=gcd%20is%20%3A%2012-,math.gcd()%20%ED%95%A8%EC%88%98%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC%20Python%EC%97%90%EC%84%9C%20%EC%B5%9C%EB%8C%80,Python%20%EC%BD%94%EB%93%9C%EB%A1%9C%20%EA%B0%80%EC%A0%B8%EC%99%80%EC%95%BC%20%ED%95%A9%EB%8B%88%EB%8B%A4.)

## 참고자료
* https://velog.io/@aonee/Python-%EC%A0%95%EB%A0%AC-sort-sorted-reverse
* https://blockdmask.tistory.com/531 
