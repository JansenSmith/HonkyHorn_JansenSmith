// code here

import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
import com.neuronrobotics.bowlerkernel.Bezier3d.*;
import eu.mihosoft.vrl.v3d.*;
def URL="https://github.com/madhephaestus/HonkyHorn.git"

//Git stored file loaded but not saved
def numBezierPieces = 20
BezierEditor editor = new BezierEditor(ScriptingEngine.fileFromGit(URL, "bez.json"),10)
BezierEditor editor2 = new BezierEditor(ScriptingEngine.fileFromGit(URL, "bez2.json"),numBezierPieces)
editor2.setStartManip(editor.getEndManip())
//Git file loaded and saved. THis will do a git call on each event of change
//BezierEditor editor = new BezierEditor(URL, file,10)

ArrayList<Transform>  transforms = editor.transforms()
ArrayList<Transform>  transforms2 = editor2.transforms()

CSG makeHorn(def transforms,def transforms2, double rad, BezierEditor toAdd1, BezierEditor toAdd2) {
	def sectionOneParts =[]
	def sectionTwoParts = []
	for(int i=0;i<transforms.size();i++) {
		rad +=1;
		sectionOneParts.add(new Cylinder(rad,rad, 2,8).toCSG().roty(90));
	}
	for(int i=0;i<transforms2.size();i++) {
		sectionTwoParts.add(new Cylinder(rad,rad, 2,8).toCSG().roty(90));
		rad +=(i);
	}
	if(toAdd1!=null) {
		toAdd1.setPartsInternal(sectionOneParts)
	}
	if(toAdd2!=null) {
		toAdd2.setPartsInternal(sectionTwoParts)
	}
	def bell = CSG.unionAll(Extrude.hull(sectionTwoParts, transforms2))

	return CSG.unionAll(Extrude.hull(sectionOneParts, transforms))
			.union(bell)
			
}


CSG modelParts = makeHorn( transforms, transforms2, 10,editor,editor2)
					.difference( makeHorn( transforms, transforms2, 6,null,null))

modelParts.setName("Horn")

return [
	editor.get(),
	editor2.get(),
	modelParts
]