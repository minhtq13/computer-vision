export const appPath = {
	notFound: "*",
	default: "/",
	home: "/home",
	login: "/login",
	register: "/register",
	studentDashboard: "/student-dashboard",
	teacherDashboard: "/teacher-dashboard",
	adminDashboard: "admin-dashboard",
	//student
	studentList: "/student-list",
	studentView: "/student-view",
	studentAdd: "/student-add",
	studentEdit: "/student-edit",
	//teacher
	teacherList: "/teacher-list",
	teacherView: "/teacher-view",
	teacherEdit: "/teacher-edit",
	//subject
	subjectList: "/subject-list",
	subjectAdd: "/subject-add",
	subjectEdit: "/subject-edit",
	subjectView: "/subject-view",
	//exam
	examList: "/exam-list",
	automaticScoring: "/automatic-scoring",
	timeTable: "/time-table",
	library: "/library",
	//question
	listQuestions: "/question-list",
	addQuestions: "/question-add",
	questionList: "/question-list",
	questionEdit: "/question-edit",
	//test
	testCreate: "/test-create",
	testList: "/test-list",
	testSetCreate: "/test-set-create",
	testEdit: "/test-set/edit",
	//exam class
	examClassCreate: "/exam-class-create",
	examClassList: "/exam-class-list",
	examClassDetail: "/exam-class/details",
	examClassEdit: "/exam-class-edit",
	// user
	createUser: "/create-user",
	profileUser: "/profile-user",
	adminList: "/admin-list",
	adminEdit: "/admin-edit",

	// student-test
	studentTestList: "/student-test-list",
	onlineStudentTestDetails: "/online-test/student-test-details",

	// department
	departmentList: "/department",

	// system configuration
	systemConfiguration: "/system-config"
};

export const rewriteWithRoutePrefix = (path)  => {
	return (process.env.REACT_APP_ROUTE_PREFIX || "/v1") + path;
}
