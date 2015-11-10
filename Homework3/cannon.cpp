// Douglas Stroer
// cs4410 HW3
// 10/11/2015

#include <windows.h>   // use as needed for your system
#include <gl/Gl.h>
#include <gl/GLU.h>
#include <gl/glut.h>
#include <ctime>

void sphere(float, float, float, float, int, int);
void setWindow(float, float, float, float);
void setViewport(int, int, int, int);
void myKeyboard(unsigned char);
void displaySolid(void);


//<<<<<<<<<<<<<<<<<<<<<<<< setWindow >>>>>>>>>>>>>>>>>
void setWindow(float left, float right, float bottom, float top)
{
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluOrtho2D(left, right, bottom, top);
}
//<<<<<<<<<<<<<<<<<<<<<<<< setViewport >>>>>>>>>>>>>>>>>
void setViewport(int left, int right, int bottom, int top)
{
	glViewport(left, bottom, right - left, top - bottom);
}

//<<<<<<<<<<<<<<<<<<<<<<<< myKeyboard >>>>>>>>>>>>>>>>>
void myKeyboard(unsigned char key, int x, int y)
{
	if (key == 'q')
		exit(0);
}

//<<<<<<<<<<<<<<<<<<<<<<< GLintPoint Class >>>>>>>>>>>>>>>>>>>>
class GLintPoint {
public:
	GLint x, y, z;
	GLfloat a, b, c;
};

static GLintPoint Cannonjuice[12];	//	Global Static Array used to store sphere coordinates


//<<<<<<<<<<<<<<<<<<<<<<<< myDisplay >>>>>>>>>>>>>>>>>>>>>>
void myDisplay(void) {

	glutKeyboardFunc(myKeyboard);	// keyboard function to quit

	//set properties of the surface material
	GLfloat mat_ambient[] = { 0.0f, 0.0f, 0.0f, 1.0f };  //gray
	GLfloat mat_diffuse[] = { 0.6f, 0.6f, 0.6f, 1.0f };
	GLfloat mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	GLfloat mat_shininess[] = { 50.0f };
	glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
	glMaterialfv(GL_FRONT, GL_DIFFUSE, mat_diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
	glMaterialfv(GL_FRONT, GL_SHININESS, mat_shininess);

	// set the light source properties
	GLfloat lightIntensity[] = { 0.7f, 0.7f, 0.7f, 1.0f };
	GLfloat light_position[] = { 2.0f, 6.0f, 3.0f, 0.0f };
	glLightfv(GL_LIGHT0, GL_POSITION, light_position);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, lightIntensity);

	// set the view volume (perspective)
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(60, 1.5, 0, 20); //view angle, Aspect ratio, Near plane, Far plane



}

//<<<<<<<<<<<<<<<<<<<<<<<< main >>>>>>>>>>>>>>>>>>>>>>
void main(int argc, char** argv)
{
	glutInit(&argc, argv);          // initialize the toolkit
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH); // set display mode
	glutInitWindowSize(1000, 6000);     // set window size
	glutInitWindowPosition(100, 150); // set window position on screen
	glutCreateWindow("Cannon"); // open the screen window
	myDisplay();					//	call initial function to create random positions/colors
	glutDisplayFunc(myDisplay);     // register redraw function
	glEnable(GL_LIGHTING);	// enable the light source
	glEnable(GL_LIGHT0);
	glShadeModel(GL_SMOOTH);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_NORMALIZE);
	glClearColor(0.7f, 0.7f, 0.7f, 0.7f);	// background color

	glutIdleFunc(myDisplay);		// RedrawFunction
	glutMainLoop();             // go into a loop
}