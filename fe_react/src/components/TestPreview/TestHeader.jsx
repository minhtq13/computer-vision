export const TestHeader = ({ testDetail, testNo }) => {
	// Đối tượng JavaScript đại diện cho các thuộc tính CSS

	const contentStyle = {
		display: "flex",
		flexDirection: "column",
		alignItems: "center",
	};

	const testTop = {
		display: "flex",
		justifyContent: "space-between",
		padding: "20px 40px 20px 30px",
	};

	const testBgd = {
		borderBottom: "1px solid black",
		display: "flex",
		flexDirection: "column",
		alignItems: "center",
	};

	const textBold = {
		fontWeight: "bold",
	};

	const fontItalic = {
		fontStyle: "italic"
	}

	const textBoldMarginBottom = {
		fontWeight: "bold",
		marginBottom: "12px",
	}

	const testHeaderContent = {
		display: "flex",
		justifyContent: "space-between",
		padding: "0px 15px",
		marginBottom: "20px",
	};

	return (
		<div className="test-preview">
			<div className="test-top" style={testTop}>
				<div className="test-bgd" style={testBgd}>
					<p style={textBold}>BỘ GIÁO DỤC VÀ ĐÀO TẠO</p>
					<p className="text-bold" style={textBoldMarginBottom}>
						ĐẠI HỌC BÁCH KHOA HÀ NỘI
					</p>
				</div>
				<div className="test-semester" style={testBgd}>
					<p className="text-bold" style={textBold}>
						ĐỀ THI CUỐI KỲ
					</p>
					<p className="text-bold" style={textBoldMarginBottom}>
						{`HỌC KỲ: ${testDetail.semester}`}
					</p>
				</div>
			</div>
			<div className="test-header-content" style={testHeaderContent}>
				<div className="test-header-content-left">
					<p style={textBold}>Hình thức tổ chức thi: Trắc nghiệm</p>
					<p
						style={textBold}
					>{`Mã học phần: ${testDetail.subjectCode}`}</p>
					<p
						style={textBold}
					>{`Tên học phần: ${testDetail.subjectTitle}`}</p>
				</div>
				<div className="test-header-content-right" style={contentStyle}>
					<p
						className="text-bold"
						style={textBold}
					>{`Thời gian làm bài: ${testDetail.duration} phút`}</p>
					<p style={fontItalic}>({`${testDetail?.isAllowedUsingDocuments ? "Được" : "Không"} sử dụng tài liệu`})</p>
					<p
						className="text-bold"
						style={textBold}
					>{`Mã đề: ${testNo}`}</p>
				</div>
			</div>
		</div>
	);
};

export default TestHeader;
