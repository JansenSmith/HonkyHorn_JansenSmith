// code here

import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
import com.neuronrobotics.bowlerkernel.Bezier3d.*;
import eu.mihosoft.vrl.v3d.*;
def URL="https://github.com/madhephaestus/HonkyHorn.git"

//Git stored file loaded but not saved
BezierEditor editor = new BezierEditor(URL, "bez.json",20)
BezierEditor editor2 = new BezierEditor(URL, "bez2.json",20)
editor.setStartManip(editor2.getEndManip()) 
//Git file loaded and saved. THis will do a git call on each event of change
//BezierEditor editor = new BezierEditor(URL, file,10)

ArrayList<Transform>  transforms = editor.transforms()

def sectionOneParts =[]
def sectionTwoParts = []
double rad = 10
for(int i=0;i<transforms.size();i++) {
	rad +=0.1;
	sectionOneParts.add(new Cylinder(rad, 0.01).toCSG());
}
for(int i=0;i<transforms.size();i++) {
	sectionTwoParts.add(new Cylinder(rad, 0.01).toCSG());
	rad +=0.2;
}
def modelParts = CSG.unionAll(Extrude.hull(sectionOneParts, transforms))

modelParts.setName("Horn")

return [editor.get(),editor2.get(),modelParts]