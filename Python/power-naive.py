def power(m, n):
    """Compute m ** n for a natural number n"""
    result = 1
    for i in range(n):
        result *= m
    return result

for n in range(1, 64):
    print(power(2, n))
    print(power(3, n))

