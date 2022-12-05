import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Survey } from "./Survey";
import { Home } from "./Pages/Home";
import { Col, Row } from "antd";
import { EligiblePrograms } from "./Pages/EligiblePrograms";
import { AllPrograms } from "./Pages/AllPrograms";

function App() {
  return (
    <Row className="App">
      <Col span={24}>
        <Router>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/survey" element={<Survey />} />
            <Route path="/programs" element={<EligiblePrograms />} />
            <Route path="/allprograms" element={<AllPrograms />} />
          </Routes>
        </Router>
      </Col>
    </Row>
  );
}

export default App;
