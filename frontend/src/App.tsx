import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Survey } from "./Survey";
import { Home } from "./Pages/Home";
import { Col, Row } from "antd";
import { EvaluateTest } from "./Pages/EvaluateTest";

function App() {
  return (
    <Row className="App">
      <Col span={24} className="app-wrapper">
        <Router>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/survey" element={<Survey />} />
            <Route path="/results" element={<EvaluateTest />} />
          </Routes>
        </Router>
      </Col>
    </Row>
  );
}

export default App;
