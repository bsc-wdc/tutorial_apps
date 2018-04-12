""" Words generator """
import sys
import os
from loremipsum import *


if __name__ == "__main__":
    numFiles = int(sys.argv[1])
    size = int(sys.argv[2])*1024*1024  # Mbytes to bytes
    pathDataset = "dataset_{}f_{}mb".format(numFiles, (size/1024)/1024)
    os.mkdir(pathDataset)

    for i in xrange(numFiles):
        sizeText = 0
        text = ""
        fileName = "file{}.txt".format(i)
        path = os.path.join(pathDataset, fileName)
        with open(path, 'w') as f:
            while sizeText < size:
                paragraph = generate_paragraph()
                f.write(paragraph[2])
                sizeText = os.path.getsize(path)
        print "{} generated: size {} mb".format(fileName, (float(sizeText)/1024.0)/1024.0)

