def power(m, n):
    """compute m ** n"""
    if n == 0:
        return 1
    p = power(m, n // 2)
    if n % 2 == 0:
        return p * p
    else: 
        return p * p * m;

for n in range(1, 64):
    print(power(2, n))
    print(power(3, n))


