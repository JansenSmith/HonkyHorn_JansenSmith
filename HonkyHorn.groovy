// code here

import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
import com.neuronrobotics.bowlerkernel.Bezier3d.*;
import eu.mihosoft.vrl.v3d.*;
def URL="https://github.com/madhephaestus/HonkyHorn.git"

//Git stored file loaded but not saved
BezierEditor editor = new BezierEditor(URL, "bez.json",20)
BezierEditor editor2 = new BezierEditor(URL, "bez2.json",20)
//Git file loaded and saved. THis will do a git call on each event of change
//BezierEditor editor = new BezierEditor(URL, file,10)

ArrayList<Transform>  transforms = editor.transforms()

def modelParts = CSG.unionAll(Extrude.hull(new Cube(20).toCSG(), transforms))

modelParts.setName("Horn")

return [editor.get(),editor2.get(),modelParts]