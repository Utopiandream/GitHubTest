// Douglas Stroer
// cs4410 HW2
// 15/10/2015

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

static GLintPoint juice[12];

//<<<<<<<<<<<<<<<<<<<<<<<< myDisplay >>>>>>>>>>>>>>>>>
void myDisplay(void)
{
	srand((unsigned)time(0));		//	time used to help randomize spheres
	//set properties of the surface material
	GLfloat mat_ambient[] = { 0.7f, 0.7f, 0.7f, 1.0f };  //gray
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

	glMatrixMode(GL_PROJECTION);	// set the view volume (perspective)
	glLoadIdentity();
	gluPerspective(60, 1.5, 0.3, 50); //view angle, Aspect ratio, Near plane, Far plane

	glMatrixMode(GL_MODELVIEW);		// set the camera
	glLoadIdentity();
	gluLookAt(3, 3, 0, 2.5, 2.5, 10, 2.5, 10.0, 2.5);	//	eye (3,3,0); look at reference (2.5, 2.5, 10); up (2.5, 10, 2.5) 

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);		// clear the screen
	GLintPoint spheres[12];
	for (int i = 0; i < 12; i++)	//draw initial spheres
	{
		spheres[i].x = (rand() % 10);		//	randomizing initial values for x,y,z of starting location
		spheres[i].y = (rand() % 10);
		spheres[i].z = ((rand() % 50) + 1);

		spheres[i].a = ((rand() % 10) + 1) / 10.0;			//randomize float numbers 1-10, then divide by 10 to get decimal
		spheres[i].b = ((rand() % 10) + 1) / 10.0;			//used to create random 3 floats for color
		spheres[i].c = ((rand() % 10) + 1) / 10.0;

		GLfloat mat_ambient[] = { spheres[i].a, spheres[i].b,spheres[i].c, 1.0f };
		glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
		juice[i] = spheres[i];		//insert initial values into shared array
		sphere(spheres[i].x, spheres[i].y, spheres[i].z, 0.1, 15, 15);
	}

	displaySolid();		//  call to function to move and recreate spheres
}

//<<<<<<<<<<<<<<<<<<<<<<<< displaySolid >>>>>>>>>>>>>>>>>
void displaySolid(void)
{
	glutKeyboardFunc(myKeyboard);	// keyboard function to quit

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);		//clear the screen before redraw

		Sleep(50);
		for (int i = 0; i < 12; i++)	//move all Spheres closer to near plane
		{
			if (juice[i].z > 1)		//	if still behind near plane
				juice[i].z -= .1;
			else {
				juice[i].z = 49;		// else reset current sphere to far plane, & randomize x and y
				juice[i].x = (rand() % 10);
				juice[i].y = (rand() % 10);
			}
			GLfloat mat_ambient[] = { juice[i].a, juice[i].b,juice[i].c, 1.0f };
			glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
			sphere(juice[i].x, juice[i].y, juice[i].z, 0.5, 15, 15);
		}
		glutSwapBuffers();		//swap buffer for smoother animation
		glutPostRedisplay();	//tell the main loop to redraw screen

}




//<<<<<<<<<<<<<<<<<<<<<<<< sphere >>>>>>>>>>>>>>>>>
void sphere(float cx, float cy, float cz, float radius, int slice, int stack)
{
					// draw the sphere
	glPushMatrix();
	glTranslated(cx, cy, cz);		//	Modify Matrix to different position
	glutSolidSphere(radius, slice, stack);		// create sphere at location
	glPopMatrix();

	glFlush();		//flush drawing to screen
}

//<<<<<<<<<<<<<<<<<<<<<<<< main >>>>>>>>>>>>>>>>>>>>>>
void main(int argc, char** argv)
{
	glutInit(&argc, argv);          // initialize the toolkit
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH); // set display mode
	glutInitWindowSize(1000, 600);     // set window size
	glutInitWindowPosition(100, 150); // set window position on screen
	glutCreateWindow("Spheres"); // open the screen window
	myDisplay();					//	call initial function to create random positions/colors
	glutDisplayFunc(displaySolid);     // register redraw function
	glEnable(GL_LIGHTING);	// enable the light source
	glEnable(GL_LIGHT0);
	glShadeModel(GL_SMOOTH);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_NORMALIZE);
	glClearColor(0.0f, 0.0f, 0.0f, 0.0f);	// background color
	
	glutIdleFunc(displaySolid);		// RedrawFunction
	glutMainLoop();             // go into a loop
}
