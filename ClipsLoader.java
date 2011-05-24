package CardGames;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;


public class ClipsLoader
{ 
    
  private HashMap clipsMap; 
    /* The key is the clip 'name', the object (value) 
       is a ClipInfo object */


  public ClipsLoader(String soundsFnm, String sSNDSDir)
  { clipsMap = new HashMap();
    loadSoundsFile(soundsFnm, sSNDSDir);
  }

  public ClipsLoader()
  {  clipsMap = new HashMap();  } 



  private void loadSoundsFile(String soundsFnm, String sSNDSDir)
  /* The file format are lines of:
        <name> <filename>         // a single sound file
     and blank lines and comment lines.
  */
  { 
    String sndsFNm = sSNDSDir + soundsFnm;
    try {
      InputStream in = this.getClass().getResourceAsStream(sndsFNm);
      BufferedReader br = new BufferedReader( new InputStreamReader(in));
      // BufferedReader br = new BufferedReader( new FileReader(sndsFNm));
      StringTokenizer tokens;
      String line, name, fnm;
      while((line = br.readLine()) != null) {
        if (line.length() == 0)  // blank line
          continue;
        if (line.startsWith("//"))   // comment
          continue;

        tokens = new StringTokenizer(line);
        if (tokens.countTokens() != 2)
          System.out.println("Wrong no. of arguments for " + line);
        else {
          name = tokens.nextToken();
          fnm = tokens.nextToken();
          load(name, fnm, sSNDSDir);
        }
      }
      br.close();
    } 
    catch (IOException e) 
    { System.out.println("Error reading file: " + sndsFNm);
      System.exit(1);
    }
  }  // end of loadSoundsFile()



  // ----------- manipulate a particular clip --------


  public void load(String name, String fnm, String sSDNSDir)
  // create a ClipInfo object for name and store it
  {
    if (clipsMap.containsKey(name))
      System.out.println( "Error: " + name + "already stored");
    else {
      clipsMap.put(name, new ClipInfo(name, fnm, sSDNSDir) );
    }
  }  // end of load()


  public void close(String name)
  // close the specified clip
  {  ClipInfo ci = (ClipInfo) clipsMap.get(name);
     if (ci == null)
       System.out.println( "Error: " + name + "not stored");
     else
      ci.close();
  } // end of close()

 

  public void play(String name, boolean toLoop)
  // play (perhaps loop) the specified clip
  {  ClipInfo ci = (ClipInfo) clipsMap.get(name);
     if (ci == null)
       System.out.println( "Error: " + name + "not stored");
     else
      ci.play(toLoop);
  } // end of play()


  public void stop(String name)
  // stop the clip, resetting it to the beginning
  { ClipInfo ci = (ClipInfo) clipsMap.get(name);
    if (ci == null)
      System.out.println( "Error: " + name + "not stored");
    else
      ci.stop();
  } // end of stop()


  public void pause(String name)
  { ClipInfo ci = (ClipInfo) clipsMap.get(name);
    if (ci == null)
      System.out.println( "Error: " + name + "not stored");
    else
      ci.pause();
  } // end of pause()


  public void resume(String name)
  { ClipInfo ci = (ClipInfo) clipsMap.get(name);
    if (ci == null)
      System.out.println( "Error: " + name + "not stored");
    else
      ci.resume();
  } // end of resume()


  // -------------------------------------------------------


  public void setWatcher(String name, SoundsWatcher sw)
  /* Set up a watcher for the clip. It will be notified when
     the clip loops or stops. */
  { ClipInfo ci = (ClipInfo) clipsMap.get(name);
    if (ci == null)
      System.out.println( "Error: " + name + "not stored");
    else
      ci.setWatcher(sw);
  } // end of setWatcher()

}  // end of ClipsLoader class
