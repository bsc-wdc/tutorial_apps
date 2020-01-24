from pycompss.api.task import task
from pycompss.api.api import compss_wait_on


@task(returns=tuple)
def compute(lst, condition):
    """
    Task that accumulates the contents of lst, maximum and minimum elements.
    If condition is True, the computation is done without problems.
    Otherwise, returns a non serializable object wich contains the exception.
    :param condition: Computation condition.
    :return: Tuple
    """
    if condition:
        return (sum(lst), max(lst), min(lst))
    else:
        import sys
        try:
            raise Exception("Intended exception to fill sys.exc_info() with traceback object")
            # Traceback objects are non serializable with pickle and dill.
        except:
            info = sys.exc_info()
            # info is a tuple of the form (type, Exception, traceback)
            return info


def main():
    # Example of with condition True (task returns a serializable object)
    obj = [1, 2, 3, 4, 5]
    a = compute(obj, True)
    a = compss_wait_on(a)
    print("Accumulated result in a: " + str(a))

    # What happens with a task that returns a non serializable object?
    b = compute(obj, False)
    b = compss_wait_on(b)
    print("Accumulated result in b: " + str(b))

if __name__ == '__main__':
    main()
