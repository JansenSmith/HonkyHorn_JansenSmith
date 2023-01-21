// code here

import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
import com.neuronrobotics.bowlerkernel.Bezier3d.*;
import eu.mihosoft.vrl.v3d.*;
def URL="https://github.com/madhephaestus/HonkyHorn.git"

//Git stored file loaded but not saved
def numBezierPieces = 20
BezierEditor editor = new BezierEditor(ScriptingEngine.fileFromGit(URL, "bez.json"),10)
BezierEditor editor2 = new BezierEditor(ScriptingEngine.fileFromGit(URL, "bez2.json"),numBezierPieces)
BezierEditor editor3 = new BezierEditor(ScriptingEngine.fileFromGit(URL, "bez3.json"),numBezierPieces)

//editor2.setStartManip(editor.getEndManip())
editor.addBezierToTheEnd(editor2)
editor.addBezierToTheEnd(editor3)
//Git file loaded and saved. THis will do a git call on each event of change
//BezierEditor editor = new BezierEditor(URL, file,10)



CSG makeHorn( double rad, BezierEditor editor, BezierEditor editor2,  BezierEditor editor3,boolean toAdd){
	
	ArrayList<Transform>  transforms = editor.transforms()
	ArrayList<Transform>  transforms2 = editor2.transforms()
	
	ArrayList<CSG> sectionOneParts =[] 
	ArrayList<CSG> sectionTwoParts = []
	ArrayList<CSG> sectionThreeParts = []
	
	for(int i=0;i<transforms.size();i++) {
		rad +=1;
		sectionOneParts.add(new Cylinder(rad,rad, 2,8).toCSG().roty(90));
	}
	for(int i=0;i<transforms2.size();i++) {
		sectionTwoParts.add(new Cylinder(rad,rad, 2,8).toCSG().roty(90));
		sectionThreeParts.add(new Cylinder(rad,rad, 2,8).toCSG().roty(90));
		rad +=(i);
	}
	if(toAdd) {
		editor.setPartsInternal(sectionOneParts)
		editor2.setPartsInternal(sectionTwoParts)
		editor3.setPartsInternal(sectionThreeParts)
	}
	def bell = CSG.unionAll(Extrude.hull(sectionTwoParts, transforms2))
	def bell2 = CSG.unionAll(Extrude.hull(sectionThreeParts, editor3.transforms()))
	return CSG.unionAll(Extrude.hull(sectionOneParts, transforms))
			.union(bell)
			.union(bell2)
			
}


CSG modelParts = makeHorn(  10,editor,editor2,editor3,	true)
					//.difference( makeHorn(  6,editor,editor2,editor3,false))

modelParts.setName("Horn")

return [
	editor.get(),
	editor2.get(),
	editor3.get(),
	//modelParts
]