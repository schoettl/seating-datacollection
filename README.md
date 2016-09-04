Seating Data Collection
=======================

This is an Android app for real-time data collection on people's seating behavior in trains.

It is configured for Munich's S-Bahn trains which have compartments with each 4 vis-à-vis seat groups.
Each seat group has 4 seats.
However it should not be too hard to adapt the app for other seating layouts.

Features
--------

- Conduct multiple surveys
- Provide meta data for surveys
- Input the initial condition
- Real-time data collection via event logging
   - Log persons sitting down, changing place, leaving
   - Define groups of persons for social studies
   - Input age and gender of persons
   - Place or remove baggage
   - Log train stops
   - Mark disturbing persons
   - Undo above actions in case of mistakes
- Export to [CSV files](https://github.com/schoettl/seating-data/tree/master/data)

Data analysis
-------------

See the [seating-data](https://github.com/schoettl/seating-data) repo for real data an well-tested R programs for data analysis.

License
-------

See [LICENSE](LICENSE).
