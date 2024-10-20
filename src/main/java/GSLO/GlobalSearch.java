package GSLO;

import util.Area;
import util.Region;

import java.util.ArrayList;

//package json;
//import io.json.JSONObject;
//
//import java.io.FileWriter;
//import java.io.IOException;

/**
 * This class corresponds to the Section 5.1 Global Search
 */
public class GlobalSearch {
    private Region[] regions;
    private ArrayList<Area> all_areas;
    private Seed seed;
    private long total_running_time;
    private long seed_time;
    private long region_growth_time;
    private long enclaves_assign_time;
    private long interregion_update_time;
    private long indirect_flow_time;
    private boolean interregion_flag;
    private boolean flow_flag;

    /**
     *
     * @param all_areas the input areas
     * @param p the predefined number of regions
     * @param selection_max_iter the maximum number of iterations in Seed Identification
     * @param threshold the value on the user-defined constraint
     * @param detect_island whether or not the input dataset includes island
     */
    public GlobalSearch(ArrayList<Area> all_areas, int p, int selection_max_iter, long threshold , boolean detect_island) throws InterruptedException {
        long start = System.currentTimeMillis();

        this.all_areas = all_areas;
        for (Area all_area : all_areas) { all_area.set_centroid(); }

        long seeding_start = System.nanoTime();
        if(!detect_island)
        {
            if(selection_max_iter >= 0)
            {
                seed = new SeedIdentification(all_areas , p , selection_max_iter , false , false).getBest_seed();
            }

            else
            {
                seed = new SeedIdentification(all_areas , p , selection_max_iter , true , false).getBest_seed();
            }
        }

        else
        {
            seed = new SeedIdentification(all_areas , p , selection_max_iter , false , true).getBest_seed();
        }

        long seeding_end = System.nanoTime();
        this.seed_time = seeding_end - seeding_start;
        int total_areas_count = 0;
        for (Area a: all_areas){
            int geo_index = a.get_geo_index();
            long isSchool = a.get_extensive_attr();
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("x", coordinate.getX());
//                    jsonObject.put("y", coordinate.getY());
//                    jsonArray.put(jsonObject);
            System.out.println(isSchool + " " + geo_index);
            total_areas_count ++;


        }
//        System.out.println(total_areas_count);
        System.out.println("this is after");

        long region_growth_start = System.nanoTime();
        regions = new RegionGrowth(seed , threshold , all_areas).grow_region_robust();
        long region_growth_end = System.nanoTime();
        this.region_growth_time = region_growth_end - region_growth_start;
        int i = 0;
        total_areas_count = 0;
        for (Region region : regions){
            for (Area a : region.get_areas_in_region()){
                total_areas_count++;
                System.out.println(i + " " + a.get_geo_index());
            }
            i++;
        }
//        System.out.println(total_areas_count);


//        long assign_enclaves_start = System.nanoTime();
//        new EnclavesAssignment(all_areas , regions);
//        long assign_enclaves_ends = System.nanoTime();
//        this.enclaves_assign_time = assign_enclaves_ends - assign_enclaves_start;


//        if(!solved())
//        {
//            long interregion_start = System.currentTimeMillis();
//            new InterregionUpdate(regions, all_areas).region_adjustment();
//            long interregion_end = System.currentTimeMillis();
//            this.interregion_update_time = interregion_end - interregion_start;
//            this.interregion_flag = true;
//        }
//
//        else
//        {
//            this.interregion_flag = false;
//        }
//
//        if(!solved())
//        {
//            long flow_start = System.currentTimeMillis();
//            new IndirectFlowPush(regions , threshold , all_areas).flow_pushing();
//            long flow_end = System.currentTimeMillis();
//            this.indirect_flow_time = flow_end - flow_start;
//            this.flow_flag = true;
//
//        }
//
//        else
//        {
//            this.flow_flag = false;
//        }



        long end = System.currentTimeMillis();

        total_running_time = end - start;
    }


    public boolean isInterregion_flag()
    {
        return interregion_flag;
    }

    public boolean isFlow_flag()
    {
        return flow_flag;
    }

    public long getSeed_time()
    {
        return seed_time;
    }

    public long getRegion_growth_time()
    {
        return region_growth_time;
    }

    public long getEnclaves_assign_time()
    {
        return enclaves_assign_time;
    }

    public long getInterregion_update_time()
    {
        return interregion_update_time;
    }

    public long getIndirect_flow_time()
    {
        return indirect_flow_time;
    }


    public ArrayList<Area> get_all_areas()
    {
        return all_areas;
    }

    public Region[] get_regions()
    {
        return regions;
    }

    public long getTotal_running_time()
    {
        return total_running_time;
    }

    public boolean solved()
    {
        for(Region r : regions)
        {
            if(!r.is_region_complete())
            {
                return false;
            }
        }
        return true;
    }

    public double get_seed_quality()
    {
        return seed.get_min_dist();
    }


}
