## This project is an Android app demonstrating the A\* Algorithm.

![Alt Text](preview.gif)

It uses sophisticated datastructures to improve runtime performance. This makes to possible to find the shortest path from *A* to *B* for a grid size of 200 times 200 tiles in about 200 milliseconds (tested on Samsung Galaxy S8). Here are some further details:

| grid size | algorithm time     |
|-----------|--------------------|
| 200x200   | ~200 milliseconds  |
| 540x540   | ~500 milliseconds  |
| 1080x1080 | ~1900 milliseconds |

Notice that the app has to render all tiles as well. The render time is not included in the estimated algorithm time.

Have fun playing around!
