package util;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class preprocess the data by reading the data from the shapefile, creating the area class and build the neighboring relations
 */
public class Preprocess {


    public static ArrayList<Area> GeoSetBuilder(String dataset) throws IOException {
        ArrayList<Area> areas = new ArrayList<>();
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = null;
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection1 = null;
        //=================================================================================================
        if(dataset.equals("redistricting3") || dataset.equals("redistrictingES") || dataset.equals("redistrictingMS") || dataset.equals("redistrictingHS")){
            collection = preprocess("redistricting1");
            collection1 = preprocess("redistricting2");
        }
        //=============================================================================================================
        else{
            collection = preprocess(dataset);
        }
        ArrayList<Geometry> polygons = initial_construct(collection, collection1, areas, dataset);
        setNeighbors(polygons , areas);
        return areas;

    }

    private static FeatureCollection<SimpleFeatureType, SimpleFeature> preprocess(String dataset) throws IOException {

        File file = null;
        switch (dataset) {
            case "2k":
                file = new File("DataFile/2056dataset/merged.shp");
                break;
            case "diversity":
                file = new File("DataFile/diversity/2000data.shp");
                break;
            case "island":
                file = new File("DataFile/islanddata/WAandPENN.shp");
                break;
            case "5k":
                file = new File("DataFile/5K/5K.shp");
                break;
            case "10k":
                file = new File("DataFile/10K/10K.shp");
                break;
            case "20k":
                file = new File("DataFile/20K/20K.shp");
                break;
            case "30k":
                file = new File("DataFile/30K/30K.shp");
                break;
            case "40k":
                file = new File("DataFile/40K/40K.shp");
                break;
            case "50k":
                file = new File("DataFile/50K/50K.shp");
                break;
            case "60k":
                file = new File("DataFile/60K/60K.shp");
                break;
            case "70k":
                file = new File("DataFile/70K/70K.shp");
                break;
            case "80k":
                file = new File("DataFile/80K/80K.shp");
                break;
            case "redistricting1":
                file = new File("DataFile/redistricting/SPAs-polygon.shp");
                break;
            case "redistricting2":
                file = new File("DataFile/redistricting/Schools-point.shp");
                break;
        }
        //System.out.println(file.getTotalSpace());
        Map<String, Object> map = new HashMap<>();
        map.put("url", file.toURI().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source =
                dataStore.getFeatureSource(typeName);
        Filter filter = Filter.INCLUDE;
        dataStore.dispose();
        //================================================================================
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
        System.out.println(collection.getSchema().getAttributeCount());
        System.out.println(collection.getSchema().toString());
        return collection;
    }

    private static ArrayList<Geometry> initial_construct(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, FeatureCollection<SimpleFeatureType, SimpleFeature> optionalCollection, ArrayList<Area> areas, String dataset)
    {
        ArrayList<Geometry> polygons = new ArrayList<>();
        long geo_index = 1;
        int spa_num = 0;
        int num_schools = 0;
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                spa_num++;
                SimpleFeature feature = features.next();
                long extensive_attr ;
                long internal_attr;
                long extensive_attr_school_cap = -1;

                if(dataset.equals("2k"))
                {
                    extensive_attr = Long.parseLong((feature.getAttribute("aland").toString()));
                    internal_attr  = Long.parseLong(feature.getAttribute("awater").toString());
                }

                else if(dataset.equals("diversity"))
                {
                    extensive_attr = (long)Double.parseDouble((feature.getAttribute("cty_pop200").toString()));
                    internal_attr = (long)(1000 * Double.parseDouble((feature.getAttribute("ratio").toString())));

                }//======================================================================
                else if (dataset.equals("redistricting1"))
                {
                    extensive_attr = Long.parseLong((feature.getAttribute("ELEM_POP").toString()));
                    internal_attr  = Long.parseLong(feature.getAttribute("HIGH_POP").toString());
                }
                else if (dataset.equals("redistricting2"))
                {
                    extensive_attr = Long.parseLong((feature.getAttribute("MID_").toString()));
                    internal_attr  = Long.parseLong(feature.getAttribute("HIGH_").toString());
                }
                else if(dataset.startsWith("redistricting")){
                    String requiredSchoolType = dataset.substring(dataset.length()-2);
                    extensive_attr = 0;
                    try (SimpleFeatureIterator school_iterator = ((SimpleFeatureCollection)optionalCollection).features()) {
                        while (school_iterator.hasNext()) {
                            SimpleFeature school_feature = school_iterator.next();
                            String schoolSPA  = school_feature.getAttribute("SPA").toString();
                            String schoolType = school_feature.getAttribute("SCHOOL_TYP").toString();
                            if (feature.getAttribute("SPA").equals(schoolSPA) && schoolType.equals(requiredSchoolType)){
                                System.out.println(requiredSchoolType + " found in SPA " + geo_index + ", num " + requiredSchoolType + " matched:" + num_schools);
                                extensive_attr = 1;
                                num_schools++;
                                extensive_attr_school_cap = Long.parseLong(school_feature.getAttribute("CAPACITY").toString());
                            }
                        }
                    }
                    String pop = requiredSchoolType.replace("ES", "ELEM_POP");
                    pop = pop.replace("MS", "MID_POP");
                    pop = pop.replace("HS", "HIGH_POP");
                    internal_attr  = Long.parseLong(feature.getAttribute(pop).toString());
                }
                else
                {
                    extensive_attr = Long.parseLong((feature.getAttribute("ALAND").toString()));
                    internal_attr  = Long.parseLong(feature.getAttribute("AWATER").toString());
                }

                Geometry polygon = (Geometry) feature.getDefaultGeometry();
                polygons.add(polygon);
                Coordinate[] coor = polygon.getCoordinates();
                geo_index = Long.parseLong((feature.getAttribute("OBJECTID").toString()));
                Area newArea = new Area((int)geo_index , internal_attr , extensive_attr , extensive_attr_school_cap , coor);
                areas.add(newArea);
            }
        }

        return polygons;

    }

    private static void setNeighbors(ArrayList<Geometry> polygons , ArrayList<Area> areas)
    {
        for (int i = 0; i < polygons.size(); i++) {

            for (int j = i + 1; j < polygons.size(); j++) {

                if (polygons.get(i).intersects(polygons.get(j))) {

                    Geometry intersection = polygons.get(i).intersection(polygons.get(j));

                    if (intersection.getGeometryType() != "Point") {

                        areas.get(i).add_neighbor(j);
                        areas.get(j).add_neighbor(i);


                    }
                }
            }
        }



    }
}
