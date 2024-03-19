package util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class describes the Area class which is a spatial area that has numerical attribute and represented by a set of marginal coordinates
 */
public class Area implements Cloneable , Serializable {

    private int index;
    private long sim_attr;
    private long extensive_attr;
    //======================================================================================
    private long similarity_attr_school_cap;
    //======================================================================================
    private Coordinate[] coor_array;
    private Coordinate centroid;
    private ArrayList<Integer> neigh_area_index;
    private int associate_region_index;

    /**
     *
     * @param index the unique identifier of an area
     * @param sim_attr the similarity attribute
     * @param extensive_attr the extensive attribute
     * @param coor_array the set of coordinates that marks the margin of this area
     */
    public Area(int index , long sim_attr, long extensive_attr, Coordinate[] coor_array)
    {
        this.index = index;
        this.sim_attr = sim_attr;
        this.extensive_attr = extensive_attr;
        this.similarity_attr_school_cap = -1;
        this.coor_array = coor_array;
        neigh_area_index = new ArrayList<>();
        associate_region_index = -1;
    }

    public Area(int index , long sim_attr, long extensive_attr, long similarity_attr_school_cap, Coordinate[] coor_array)
    {
        this.index = index;
        this.sim_attr = sim_attr;
        this.extensive_attr = extensive_attr;
        this.similarity_attr_school_cap = similarity_attr_school_cap;
        this.coor_array = coor_array;
        neigh_area_index = new ArrayList<>();
        associate_region_index = -1;
    }



    public void set_centroid()
    {
        double total_x = 0.0;
        double total_y = 0.0;
        for (Coordinate coordinate : coor_array) {
            total_x += coordinate.getX();
            total_y += coordinate.getY();
        }
        double ave_x = total_x / coor_array.length;
        double ave_y = total_y / coor_array.length;
        centroid = new Coordinate(ave_x , ave_y);
    }

    /**
     *
     * @param a the area to compute distance with
     * @return the
     * distance between this area and area a
     */
    public double compute_dist(Area a)
    {
        Coordinate a_centroid = a.get_centroid();
        return  Math.sqrt((centroid.getX() - a.get_centroid().getX()) * (centroid.getX() - a.get_centroid().getX()) + (centroid.getY() - a_centroid.getY()) * (centroid.getY() - a_centroid.getY()));

    }

    public void set_centroid(Coordinate centroid)
    {
        this.centroid = centroid;
    }

    public void set_region(int region_index)
    {
        this.associate_region_index = region_index;
    }

    public void add_neighbor(int add_index)
    {
        neigh_area_index.add(add_index);
    }

    public void set_neighbor_once(ArrayList<Integer> neighbor_to_set)
    {
        this.neigh_area_index = neighbor_to_set;
    }

    public int get_geo_index() { return index; }

    public long get_internal_attr()
    {
        return sim_attr;
    }

    public long get_extensive_attr()
    {
        return extensive_attr;
    }
    public long get_similarity_attr_school_cap() { return similarity_attr_school_cap; }
    public long get_sim_attr() { return sim_attr; }



    public ArrayList<Area> get_neigh_area(ArrayList<Area> all_areas) {
        ArrayList<Area> neigh_areas = new ArrayList<>();
        for(int neigh_index : neigh_area_index)
        {
            neigh_areas.add(all_areas.get(neigh_index));
        }
        return neigh_areas;
    }

    public ArrayList<Integer> get_neigh_area_index()
    {
        return neigh_area_index;
    }

    public int get_associated_region_index() { return associate_region_index; }

    public Coordinate[] get_coordinates() { return coor_array; }

    public Coordinate get_centroid() { return centroid; }

    public Polygon get_polygon() {
        // Create a GeometryFactory
        GeometryFactory geometryFactory = new GeometryFactory();

// Create a LinearRing from the Coordinate array
        LinearRing linearRing = geometryFactory.createLinearRing(coor_array);

// Create a Polygon from the LinearRing
        Polygon polygon = geometryFactory.createPolygon(linearRing);
        return polygon;
    }

    public double get_area(){
        Polygon polygon = get_polygon();
        return polygon.getArea();
    }


    public long compute_hetero(Area neigh_area) {
        return Math.abs(sim_attr - neigh_area.get_internal_attr());
    }

    public void initialize_neighbor() {
        neigh_area_index = new ArrayList<>();
    }



    @Override
    protected Object clone() {
        Area g = new Area(this.get_geo_index() , this.get_internal_attr() , this.get_extensive_attr() , this.get_coordinates());
        g.set_region(this.get_associated_region_index());
        g.set_neighbor_once((ArrayList<Integer>)neigh_area_index.clone());
        g.set_centroid(this.get_centroid());
        return g;
    }


    public static ArrayList<Area> area_list_copy(ArrayList<Area> all_areas) throws CloneNotSupportedException {
        ArrayList<Area> returned_areas = new ArrayList<>();
        for(Area g : all_areas)
        {
            returned_areas.add((Area)g.clone());
        }
        return returned_areas;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Area)) return false;
        return this.get_geo_index() == ((Area) o).get_geo_index();
    }





}
