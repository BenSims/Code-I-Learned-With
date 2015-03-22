package mygame;
 /*
  * Benjamin Sims
  * Tyler Cheatham
  * Computer Science 325 section 6980
  * University of Maryland University College
  * Final Project - Hover Tank Targets
  * Prof. Mark Wireman
  * Creation Date: 7/15/2013
  * Last Modification: 7/26/2013
  * Description: A game where you play as a hovering tank who's mission is to
  * shoot as many bouncing targets as possible in 5 minutes and beat the 
  * high score.
  * 
  * JMONKEY LEGAL--------
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.*;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer.CompareMode;
import com.jme3.shadow.PssmShadowRenderer.FilterMode;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinalGame extends SimpleApplication implements AnalogListener,
        ActionListener, PhysicsCollisionListener {

    private BulletAppState bulletAppState;
    private PhysicsHoverControl hoverControl;
    private Spatial tank;
    TerrainQuad terrain;
    Material matRock;
    boolean wireframe = false;
    protected BitmapText hintText;
    PointLight pl;
    Geometry lightMdl;
    Geometry collisionMarker;

    //New stuff I'm adding
    private RigidBodyControl sphere_phy;
     private RigidBodyControl cube_phy;
    private static Sphere sphere;  
    private static Box cube;
    private Material jupiter_mat;
    private Material sky_mat;
    private Material earth_mat;
    private Material fun_mat;
    private Material wall_mat;

    //Scores for the object
    private static int skyScore = 0;
    private double faceScore = 0;
    private int earthScore = 0;
    private double jupiterScore = 0;
    private double finalScore = 0;
    private double highScore = 0;
    private float time = -3;
    private int shotCount = 0;
    private int hitCount = 0;
    

    public static void main(String[] args) {
        FinalGame app = new FinalGame();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL3);

        //read the highscore before starting the game. 
        try {        
        app.readHighScore();  // this reads the scores.txt file 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FinalGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FinalGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
                Logger.getLogger(FinalGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //start the game!
        app.start();
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    private void setupKeys() {
        inputManager.addMapping("START", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "Reset");
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager); DEBUGING FRAME
        bulletAppState.getPhysicsSpace().setAccuracy(1f/30f);
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        //shadow effects
        PssmShadowRenderer pssmr = new PssmShadowRenderer(assetManager, 2048, 3);
        pssmr.setDirection(new Vector3f(-0.5f, -0.3f, -0.3f).normalizeLocal());
        pssmr.setLambda(0.55f);
        pssmr.setShadowIntensity(0.6f);
        pssmr.setCompareMode(CompareMode.Hardware);
        pssmr.setFilterMode(FilterMode.Bilinear);
        viewPort.addProcessor(pssmr); 
        
        //build my pretty... build!
        initMaterials();
        setupKeys();
        createTerrain();
        theGame();    
    }
    
    public void theGame(){
        //create the tank
        buildPlayer();
        
        //create the targets
        initSphere("Jupiter", 32, 32, 3f, -140, 14, 0, jupiter_mat);
        initCube("Space", 3, 3, 3, -140, 14, 20, 20, sky_mat);
        initSphere("Earth", 22, 22, 2f, -120, 14, -20, earth_mat);
        initCube("Face", 2, 2, 2, -120, 14, 40, 10, fun_mat);
        //create the walls
        initCube("mark", 500, 50, 2, 0, 50, 500, 0, wall_mat);
        initCube("mark2", 2, 50, 500, 500, 50, 0, 0, wall_mat);
        initCube("mark", 500, 50, 2, 0, 50, -500, 0, wall_mat);
        initCube("mark2", 2, 50, 500, -500, 50, 0, 0, wall_mat);

        //light for models and terrain
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(new ColorRGBA(1.0f, 0.94f, 0.8f, 1f).multLocal(1.3f));
        dl.setDirection(new Vector3f(-0.5f, -0.3f, -0.3f).normalizeLocal());
        rootNode.addLight(dl);

        Vector3f lightDir2 = new Vector3f(0.70518064f, 0.5902297f, -0.39287305f);
        DirectionalLight dl2 = new DirectionalLight();
        dl2.setColor(new ColorRGBA(0.7f, 0.85f, 1.0f, 1f));
        dl2.setDirection(lightDir2);
        rootNode.addLight(dl2);           
    }
    //this is the method called when the game timer ends
    public void gameOver(){
        this.rootNode.detachAllChildren();
        finalScore = skyScore + faceScore + jupiterScore + earthScore;
        
        /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        BitmapText gameOver = new BitmapText(guiFont, false);
        gameOver.setSize(guiFont.getCharSet().getRenderedSize());
        gameOver.setText("-Game Over-" + "\n\nYour Score is: " + finalScore + " points.");
        gameOver.setLocalTranslation(200, 350, 0);
        guiNode.attachChild(gameOver);
        //this next part is for beating the high score
        BitmapText finalscore = new BitmapText(guiFont, false);
        finalscore.setSize(guiFont.getCharSet().getRenderedSize());
        finalscore.setText("--->>  NEW RECORD!!!  <<---");
        finalscore.setLocalTranslation(200, 400, 0);
        
        if (finalScore > highScore){
            guiNode.attachChild(finalscore);
            try {        
                writeHighScore();   //this writes the new high score to file
             } catch (FileNotFoundException ex) {
               Logger.getLogger(FinalGame.class.getName()).log(Level.SEVERE, null, ex);
             } catch (UnsupportedEncodingException ex) {
               Logger.getLogger(FinalGame.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
               Logger.getLogger(FinalGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
    }
    //code for building the tank
    private void buildPlayer() {
        tank = assetManager.loadModel("Models/HoverTank/Tank2.mesh.xml");
        CollisionShape colShape = CollisionShapeFactory.createDynamicMeshShape(tank);
        tank.setShadowMode(ShadowMode.CastAndReceive);
        tank.setLocalTranslation(new Vector3f(-140, 14, -23));
        tank.setLocalRotation(new Quaternion(new float[]{0, 0.01f, 0}));
        hoverControl = new PhysicsHoverControl(colShape, 500);
        hoverControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        tank.addControl(hoverControl);
        rootNode.attachChild(tank);
        getPhysicsSpace().add(hoverControl);
        //camera
        ChaseCamera chaseCam = new ChaseCamera(cam, inputManager);
        tank.addControl(chaseCam);
        flyCam.setEnabled(false);
    }
    //code for building the ammo (missiles)
    public void makeMissile() {
        Vector3f pos = tank.getWorldTranslation().clone();
        Quaternion rot = tank.getWorldRotation();
        Vector3f dir = rot.getRotationColumn(2);
        Spatial missile = assetManager.loadModel("Models/SpaceCraft/Rocket.mesh.xml");
        missile.scale(1f);
        missile.rotate(0, FastMath.PI, 0);
        missile.updateGeometricState();
        BoundingBox box = (BoundingBox) missile.getWorldBound();
        final Vector3f extent = box.getExtent(null);
        BoxCollisionShape boxShape = new BoxCollisionShape(extent);
        missile.setName("Missile");
        missile.rotate(rot);
        missile.setLocalTranslation(pos.addLocal(0, extent.y * 4.5f, 0));
        missile.setLocalRotation(hoverControl.getPhysicsRotation());
        missile.setShadowMode(ShadowMode.Cast);
        RigidBodyControl control = new BombControl(assetManager, boxShape, 20);
        control.setLinearVelocity(dir.mult(100));
        control.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        missile.addControl(control);
        rootNode.attachChild(missile);
        getPhysicsSpace().add(missile);
    }
    
    //code for building the spheres
    public void initSphere(String name, int sizeX, int sizeY, 
                            float sizeZ, int posX, int posY,
                             int posZ, Material mat){
    sphere = new Sphere(sizeX, sizeY, sizeZ, true, false);
    sphere.setTextureMode(Sphere.TextureMode.Projected);
    Geometry sphere_geo = new Geometry(name, sphere);
    sphere_geo.setMaterial(mat);
    sphere_geo.setLocalTranslation(posX, posY, posZ);
    sphere_phy = new RigidBodyControl(100f);
    sphere_geo.addControl(sphere_phy);
    bulletAppState.getPhysicsSpace().add(sphere_phy);
    this.rootNode.attachChild(sphere_geo);
  }
    
    //code for building the cubes
     public void initCube(String name, float sizeX, float sizeY, 
                            float sizeZ, int posX, int posY,
                             int posZ, int grav, Material mat) {
    cube = new Box(Vector3f.ZERO, sizeX, sizeY, sizeZ);
    cube.scaleTextureCoordinates(new Vector2f(1, 1));
    Geometry cube_geo = new Geometry(name, cube);
    cube_geo.setQueueBucket(Bucket.Transparent);
    cube_geo.setMaterial(mat);
    cube_geo.setLocalTranslation(posX, posY, posZ);
    cube_phy = new RigidBodyControl(grav);
    cube_geo.addControl(cube_phy);
    bulletAppState.getPhysicsSpace().add(cube_phy);
    this.rootNode.attachChild(cube_geo); 
  }

   public void onAnalog(String binding, float value, float tpf) {
    }
    //code for reading user input, steering the tank, shooting, reseting.
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            hoverControl.steer(value ? 50f : 0);
        } else if (binding.equals("Rights")) {
            hoverControl.steer(value ? -50f : 0);
        } else if (binding.equals("Ups")) {
            hoverControl.accelerate(value ? 100f : 0);
        } else if (binding.equals("Downs")) {
            hoverControl.accelerate(value ? -100f : 0);
        } else if (binding.equals("Reset")) {
            if (value) {
                System.out.println("Reset");
                hoverControl.setPhysicsLocation(new Vector3f(-140, 14, -23));
                hoverControl.setPhysicsRotation(new Matrix3f());
                hoverControl.clearForces();
            } else {
            }
        } else if (binding.equals("Space") && value) {
            makeMissile();
            shotCount++;
        }
    }
    //taken from pg 325, Intro to Java 8th edition, Liang
    //code for reading the score.txt file, and writing to it.
    public void readHighScore() throws FileNotFoundException, UnsupportedEncodingException, IOException{
    java.io.File scores = new java.io.File("scores.txt");
    Scanner input = new Scanner(scores);
    String hScore = input.next();
    input.close();
    highScore = Double.parseDouble(hScore);    
    }
    
    public void writeHighScore() throws FileNotFoundException, UnsupportedEncodingException, IOException{
    java.io.File scores = new java.io.File("scores.txt");
    java.io.PrintWriter output = new java.io.PrintWriter(scores);
    if (finalScore > highScore){
    output.print(finalScore);
    output.close();
    }
    }
    
    //camera method from hover tank example
    public void updateCamera() {
        rootNode.updateGeometricState();
        Vector3f pos = tank.getWorldTranslation().clone();
        Quaternion rot = tank.getWorldRotation();
        Vector3f dir = rot.getRotationColumn(2);
        // make it XZ only
        Vector3f camPos = new Vector3f(dir);
        camPos.setY(0);
        camPos.normalizeLocal();
        // negate and multiply by distance from object
        camPos.negateLocal();
        camPos.multLocal(15);
        // add Y distance
        camPos.setY(2);
        camPos.addLocal(pos);
        cam.setLocation(camPos);
        Vector3f lookAt = new Vector3f(dir);
        lookAt.multLocal(7); // look at dist
        lookAt.addLocal(pos);
        cam.lookAt(lookAt, Vector3f.UNIT_Y);
    }

    @Override
    // this is all the text and time, stuff that refreshes every second
    public void simpleUpdate(float tpf) {
        time = time + tpf;
        /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText shotData = new BitmapText(guiFont, false);
        BitmapText warning = new BitmapText(guiFont, false);
        BitmapText direc = new BitmapText(guiFont, false);
        BitmapText score1 = new BitmapText(guiFont, false);
        BitmapText seconds = new BitmapText(guiFont, false);
        BitmapText hscore = new BitmapText(guiFont, false);
        shotData.setSize(guiFont.getCharSet().getRenderedSize());
        warning.setSize(guiFont.getCharSet().getRenderedSize());
        direc.setSize(guiFont.getCharSet().getRenderedSize());
        score1.setSize(guiFont.getCharSet().getRenderedSize());
        seconds.setSize(guiFont.getCharSet().getRenderedSize());
        hscore.setSize(guiFont.getCharSet().getRenderedSize());
        shotData.setText("Shots Fired: " + shotCount + "\nHits Made: " + hitCount);
        direc.setText("Welcome to Hover Tank Target!"
                    + "\nUse 'W' 'A' 'S' and 'D' to move the tank."
                    + "\nUse the mouse with the left button pressed to look around." 
                    + "\nUse 'Space' to shoot, 'Enter' to reset the tank."
                    + "\nYou have 5 minutes to shoot as many targest as you can!!!");
        score1.setText("Sky Score: " + skyScore + "\nJupiter Score: " + jupiterScore
                +"\nFace Score: " + faceScore + "\nEarth Score: " + earthScore
                + "\nTotal Score: " + (skyScore + jupiterScore + faceScore + earthScore));
        warning.setText("You have " + (300 - (int)time) + " seconds left!");
        seconds.setText("Time: " + (int)time);
        hscore.setText("High Score: " + highScore);
        shotData.setLocalTranslation(0, 140, 0);
        warning.setLocalTranslation(130, 430, 0);
        direc.setLocalTranslation(130, 430, 0);
        score1.setLocalTranslation(0, 100, 0);
        seconds.setLocalTranslation(0, 160, 0);
        hscore.setLocalTranslation(450, 20, 0);
        guiNode.attachChild(shotData);
        guiNode.attachChild(direc);
        guiNode.attachChild(score1);
        guiNode.attachChild(seconds);
        guiNode.attachChild(hscore);
        
        if (time > 15){
            guiNode.detachChild(direc);
        }    
        if (time > 285){
            guiNode.attachChild(warning);
        }
        if (time > 300){
            gameOver();  //calls game over method
        }
    }
    //creates the terrain, taken from hover tank example
    private void createTerrain() {
        matRock = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matRock.setBoolean("useTriPlanarMapping", false);
        matRock.setBoolean("WardIso", true);
        matRock.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        matRock.setTexture("DiffuseMap", grass);
        matRock.setFloat("DiffuseMap_0_scale", 64);
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        matRock.setTexture("DiffuseMap_1", dirt);
        matRock.setFloat("DiffuseMap_1_scale", 16);
        Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
        rock.setWrap(WrapMode.Repeat);
        matRock.setTexture("DiffuseMap_2", rock);
        matRock.setFloat("DiffuseMap_2_scale", 128);
        Texture normalMap0 = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
        normalMap0.setWrap(WrapMode.Repeat);
        Texture normalMap1 = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
        normalMap1.setWrap(WrapMode.Repeat);
        Texture normalMap2 = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
        normalMap2.setWrap(WrapMode.Repeat);
        matRock.setTexture("NormalMap", normalMap0);
        matRock.setTexture("NormalMap_1", normalMap2);
        matRock.setTexture("NormalMap_2", normalMap2);
        AbstractHeightMap heightmap = null;
        try {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
            heightmap.load();
        } catch (Exception e) {
        }
        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
        List<Camera> cameras = new ArrayList<Camera>();
        cameras.add(getCamera());
        TerrainLodControl control = new TerrainLodControl(terrain, cameras);
        terrain.addControl(control);
        terrain.setMaterial(matRock);
        terrain.setLocalScale(new Vector3f(2, 2, 2));
        terrain.setLocked(false); // unlock it so we can edit the height
        terrain.setShadowMode(ShadowMode.CastAndReceive);
        terrain.addControl(new RigidBodyControl(0));
        rootNode.attachChild(terrain);
        getPhysicsSpace().addAll(terrain);
    }

    //creates the materials for the objects, spheres and cubes.
    public void initMaterials() {
    sky_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key4 = new TextureKey("Textures/Sky2.jpg");
    key4.setGenerateMips(true);
    Texture tex4 = assetManager.loadTexture(key4);
    tex4.setWrap(WrapMode.Repeat);
    sky_mat.setTexture("ColorMap", tex4);
    
    jupiter_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key3 = new TextureKey("Textures/Jupiter.jpg");
    key3.setGenerateMips(true);
    Texture tex3 = assetManager.loadTexture(key3);
    tex3.setWrap(WrapMode.Repeat);
    jupiter_mat.setTexture("ColorMap", tex3);
    
    earth_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key2 = new TextureKey("Textures/Earth.jpg");
    key2.setGenerateMips(true);
    Texture tex2 = assetManager.loadTexture(key2);
    tex2.setWrap(WrapMode.Repeat);
    earth_mat.setTexture("ColorMap", tex2);
    
    fun_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key7 = new TextureKey("Textures/Face.jpg");
    key7.setGenerateMips(true);
    Texture tex7 = assetManager.loadTexture(key7);
    tex7.setWrap(WrapMode.Repeat);
    fun_mat.setTexture("ColorMap", tex7);
    
    wall_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key5 = new TextureKey("Textures/shoot.png");
    key4.setGenerateMips(true);
    Texture tex5 = assetManager.loadTexture(key5);
    tex5.setWrap(WrapMode.Repeat);
    wall_mat.setTexture("ColorMap", tex5);
    wall_mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);  // !   
    }
    
    //Method gives random number to be used as point to translate with
    public int randomNum(float num){
        int number = (int)(((int)time % 10) * 53.45 - 240);
        return number;
    }
    //saved the best for last, 
    //hardest part, reads collision of the missles to the objects.
    public void collision(PhysicsCollisionEvent event) {
        if(event.getNodeA() != null && event.getNodeB() != null){
            String nodeAName = event.getNodeA().getName();
            String nodeBName = event.getNodeB().getName();
            if(("Space".equals(nodeAName) || "Space".equals(nodeBName)) && 
               ("Missile".equals(nodeAName) || "Missile".equals(nodeBName))){
                skyScore++;
                hitCount++;
                rootNode.detachChild(event.getNodeA());
                bulletAppState.getPhysicsSpace().remove(event.getNodeA().getControl(RigidBodyControl.class));
                initCube("Space", 3, 3, 3, randomNum(time), 60, randomNum(time), 10, sky_mat);
            }
            if(("Jupiter".equals(nodeAName) || "Space".equals(nodeBName)) && 
               ("Missile".equals(nodeAName) || "Missile".equals(nodeBName))){
                jupiterScore = jupiterScore + 0.2;
                hitCount++;
                rootNode.detachChild(event.getNodeA());
                bulletAppState.getPhysicsSpace().remove(event.getNodeA().getControl(RigidBodyControl.class));
                initSphere("Jupiter", 22, 22, 2f, randomNum(time), 60, randomNum(time), jupiter_mat);
                initSphere("Jupiter", 22, 22, 2f, randomNum(time), 60, randomNum(time), jupiter_mat);
            }
            if(("Face".equals(nodeAName) || "Space".equals(nodeBName)) && 
               ("Missile".equals(nodeAName) || "Missile".equals(nodeBName))){
                rootNode.detachChild(event.getNodeA());
                bulletAppState.getPhysicsSpace().remove(event.getNodeA().getControl(RigidBodyControl.class));
                faceScore = faceScore + 0.1;
                hitCount++;
                initCube("Face", 10, 10, 10, randomNum(time), 60, randomNum(time), 10, fun_mat);
            }
            if(("Earth".equals(nodeAName) || "Space".equals(nodeBName)) && 
               ("Missile".equals(nodeAName) || "Missile".equals(nodeBName))){
                earthScore++;
                hitCount++;
                bulletAppState.getPhysicsSpace().remove(event.getNodeA().getControl(RigidBodyControl.class)); 
                rootNode.detachChild(event.getNodeA());
                initSphere("Earth", 22, 22, 2f, randomNum(time), 60, randomNum(time), earth_mat);
            }
            if(("Earth".equals(nodeAName) || "Earth".equals(nodeBName)) && 
               ("terrain".equals(nodeAName) || "terrain".equals(nodeBName))){
                event.getNodeA().getControl(RigidBodyControl.class).setLinearVelocity(Vector3f.UNIT_XYZ
                        .add(1, 10, 0));
            }
            if(("Space".equals(nodeAName) || "Space".equals(nodeBName)) && 
               ("terrain".equals(nodeAName) || "terrain".equals(nodeBName))){
                event.getNodeA().getControl(RigidBodyControl.class).setLinearVelocity(Vector3f.UNIT_XYZ
                        .add(0, 10, 0));
            }
            if(("Jupiter".equals(nodeAName) || "Jupiter".equals(nodeBName)) && 
               ("terrain".equals(nodeAName) || "terrain".equals(nodeBName))){
                event.getNodeA().getControl(RigidBodyControl.class).setLinearVelocity(Vector3f.UNIT_XYZ
                        .add(0, 10, 0));
            }
            if(("Face".equals(nodeAName) || "Face".equals(nodeBName)) && 
               ("terrain".equals(nodeAName) || "terrain".equals(nodeBName))){
                event.getNodeA().getControl(RigidBodyControl.class).setLinearVelocity(Vector3f.UNIT_XYZ
                        .add(0, 10, 0));
            }
        }
      }
       
}