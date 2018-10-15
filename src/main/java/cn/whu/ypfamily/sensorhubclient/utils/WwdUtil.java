package cn.whu.ypfamily.sensorhubclient.utils;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.SwingUtilities;

import cn.whu.ypfamily.sensorhubclient.ui.WwjPanel;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.data.BufferedImageRaster;
import gov.nasa.worldwind.data.DataRaster;
import gov.nasa.worldwind.data.DataRasterReader;
import gov.nasa.worldwind.data.DataRasterReaderFactory;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.SurfaceImageLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.SurfaceImage;
import gov.nasa.worldwindx.examples.util.ExampleUtil;

public class WwdUtil {
	
	public static void importImagery(final WwjPanel wwjPanel, String imgpath)
    {
        try
        {
            // Read the data and save it in a temp file.
            File sourceFile = ExampleUtil.saveResourceToTempFile(imgpath, ".tif");

            // Create a raster reader to read this type of file. The reader is created from the currently
            // configured factory. The factory class is specified in the Configuration, and a different one can be
            // specified there.
            DataRasterReaderFactory readerFactory
                = (DataRasterReaderFactory) WorldWind.createConfigurationComponent(
                AVKey.DATA_RASTER_READER_FACTORY_CLASS_NAME);
            DataRasterReader reader = readerFactory.findReaderFor(sourceFile, null);

            // Before reading the raster, verify that the file contains imagery.
            AVList metadata = reader.readMetadata(sourceFile, null);
            if (metadata == null || !AVKey.IMAGE.equals(metadata.getStringValue(AVKey.PIXEL_FORMAT)))
                throw new Exception("Not an image file.");

            // Read the file into the raster. read() returns potentially several rasters if there are multiple
            // files, but in this case there is only one so just use the first element of the returned array.
            DataRaster[] rasters = reader.read(sourceFile, null);
            if (rasters == null || rasters.length == 0)
                throw new Exception("Can't read the image file.");

            DataRaster raster = rasters[0];

            // Determine the sector covered by the image. This information is in the GeoTIFF file or auxiliary
            // files associated with the image file.
            final Sector sector = (Sector) raster.getValue(AVKey.SECTOR);
            if (sector == null)
                throw new Exception("No location specified with image.");

            // Request a sub-raster that contains the whole image. This step is necessary because only sub-rasters
            // are reprojected (if necessary); primary rasters are not.
            int width = raster.getWidth();
            int height = raster.getHeight();

            // getSubRaster() returns a sub-raster of the size specified by width and height for the area indicated
            // by a sector. The width, height and sector need not be the full width, height and sector of the data,
            // but we use the full values of those here because we know the full size isn't huge. If it were huge
            // it would be best to get only sub-regions as needed or install it as a tiled image layer rather than
            // merely import it.
            DataRaster subRaster = raster.getSubRaster(width, height, sector, null);

            // Tne primary raster can be disposed now that we have a sub-raster. Disposal won't affect the
            // sub-raster.
            raster.dispose();

            // Verify that the sub-raster can create a BufferedImage, then create one.
            if (!(subRaster instanceof BufferedImageRaster))
                throw new Exception("Cannot get BufferedImage.");
            BufferedImage image = ((BufferedImageRaster) subRaster).getBufferedImage();

            // The sub-raster can now be disposed. Disposal won't affect the BufferedImage.
            subRaster.dispose();

            // Create a SurfaceImage to display the image over the specified sector.
            final SurfaceImage si1 = new SurfaceImage(image, sector);

            // On the event-dispatch thread, add the imported data as an SurfaceImageLayer.
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    // Add the SurfaceImage to a layer.
                    SurfaceImageLayer layer = new SurfaceImageLayer();
                    layer.setName("Imported Surface Image");
                    layer.setPickEnabled(false);
                    layer.addRenderable(si1);

                    // Add the layer to the model and update the application's layer panel.
                    insertBeforeCompass(wwjPanel.getWwd(), layer);
                    //wwdPanel.update(wwdPanel.getWwd());

                    // Set the view to look at the imported image.
                    ExampleUtil.goTo(wwjPanel.getWwd(), sector);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
	public static void insertBeforeCompass(WorldWindow wwd, Layer layer) {
		// Insert the layer into the layer list just before the compass.
	    int compassPosition = 0;
	    LayerList layers = wwd.getModel().getLayers();
	    for (Layer l : layers)
	    {
	    	if (l instanceof CompassLayer)
	    		compassPosition = layers.indexOf(l);
	    }
	    layers.add(compassPosition, layer);
	}
	
	public static void insertBeforePlacenames(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just before the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
    }

    public static void insertAfterPlacenames(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just after the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition + 1, layer);
    }

    public static void insertBeforeLayerName(WorldWindow wwd, Layer layer, String targetName)
    {
        // Insert the layer into the layer list just before the target layer.
        int targetPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l.getName().indexOf(targetName) != -1)
            {
                targetPosition = layers.indexOf(l);
                break;
            }
        }
        layers.add(targetPosition, layer);
    }
    
	public static void removeLayer(WorldWindow wwd, Layer layer) {
		LayerList layers = wwd.getModel().getLayers();
		layers.remove(layer);
	}
	
	/**
	 * create point placemark
	 * @param lat
	 * @param lng
	 * @param alt
	 * @param imgUrl
	 * @return
	 */
	public static PointPlacemark createPointPlacemark(float lat, float lng, float alt, String imgUrl) {
		PointPlacemark pm = new PointPlacemark(Position.fromDegrees(lat, lng, alt));
		pm.setEnableDecluttering(true);
    	pm.setLineEnabled(false);
    	pm.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
		PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
		attrs.setImageAddress(imgUrl);
        attrs.setLabelOffset(new Offset(0.9d, 0.6d, AVKey.FRACTION, AVKey.FRACTION));
		pm.setAttributes(attrs);
		return pm;
	}
}
