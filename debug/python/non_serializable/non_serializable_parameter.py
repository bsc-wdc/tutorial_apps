from pycompss.api.task import task
from pycompss.api.api import compss_wait_on


@task(returns=int)
def accumulate(lst):
    """
    Accumulate all elements of lst if they are integers. Otherwise, returns -1.
    :param lst: List of elements
    :return: Integer
    """
    if all(isinstance(o, int) for o in lst):
        return sum(lst)
    else:
        return -1


def main():
    # Example with a serializable object
    serializable_obj = [1, 2, 3, 4, 5]
    a = accumulate(serializable_obj)
    a = compss_wait_on(a)
    print("Accumulated result in a: " + str(a))

    # What happens with a non serializable object used as a task parameter?
    import sys
    try:
        raise Exception("Intended exception to fill sys.exc_info() with traceback object")
        # Traceback objects are non serializable with pickle and dill.
    except:
        info = sys.exc_info()
    non_serializable_obj = [1, 2, 3, 4, info]
    b = accumulate(non_serializable_obj)
    b = compss_wait_on(b)
    print("Accumulated result in b: " + str(b))

if __name__ == '__main__':
    main()
