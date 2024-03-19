import numpy as np
import geopandas as gpd
import matplotlib.pyplot as plt
import matplotlib as mpl
import matplotlib.colors

fig, ax = plt.subplots(figsize=(10,10))

school_level = "ES"

areas = gpd.read_file("shape_files_areas")
all_schools = gpd.read_file("shape_files_schools")
schools = all_schools.loc[all_schools["SCHOOL_TYP"] == school_level]

# areas.plot(edgecolor="red", linewidth=0.3)
# plt.axis("off")
# plt.show()

data = []
curr_seed = 0


with open("before_regions_" + school_level + ".txt","r") as fr:
    i = 0
    lines = fr.readlines()
    for info in lines:
        if(int(info[0]) == 1):
            data.append(curr_seed)
            curr_seed += 1
        else:
            data.append(-1)

areas.insert(2, "SEED", data, True)

cmap = matplotlib.colors.LinearSegmentedColormap.from_list("", ["#e4f2f9","#063970"])

# areas.plot(ax=ax, cmap=cmap, edgecolor="Black", linewidth=0.2, column="SEED")
# schools.plot(ax=ax, s=1)
# plt.axis("off")
# plt.show()

with open("after_regions_" + school_level + ".txt","r") as fr:
    i = 0
    lines = fr.readlines()
    for info in lines:
        data = info.split()
        areas.loc[areas["OBJECTID"] == int(data[1]), "SEED"] = int(data[0])

areas.plot(ax=ax, cmap=cmap, edgecolor="Black", linewidth=0.2, column="SEED")
schools.plot(ax=ax, markersize=4, color="White")
plt.axis("off")
plt.show()