import { Button, Card, Typography } from "antd";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { surveyClient } from "../client/surveyClient";
import { ProgramCard } from "../components/ProgramCard";
import { Program } from "../types";
import { None } from "../utils/None";
import "../Survey.css";

export function EligiblePrograms() {
  const [eligiblePrograms, setEligiblePrograms] = useState<Program[]>([]);
  const [allProgramsUrl, setAllProgramsUrl] = useState<string>("");

  // This state will be used to tell if a new session start is underway
  const [isLoading, setIsLoading] = useState(false);

  // We'll need to navigate to the survey page
  const navigate = useNavigate();

  const sessionId = new URLSearchParams(window.location.search).get("sessionId");
  if (None(sessionId) || sessionId === "") {
    navigate("/");
  }

  useEffect(() => {
    const getEligiblePrograms = async () => {
      if (sessionId != null) {
        const eligiblePrograms = await surveyClient.getEligiblePrograms(sessionId);
        setEligiblePrograms(eligiblePrograms);
        setAllProgramsUrl("/allprograms?sessionId=" + sessionId);
      } else {
        navigate("/");
      }
    };
    getEligiblePrograms();
  }, [eligiblePrograms.length]);

  return (
    <>
      {eligiblePrograms.length > 0 && (
        <div>
          {eligiblePrograms
            .filter((p) => p != null)
            .map((p) => (
              <ProgramCard key={p.id} program={p} />
            ))}
        </div>
      )}
      {eligiblePrograms.length === 0 && (
        <div className="sorryCard-style">
          <Typography.Title level={3}>Sorry...</Typography.Title>
          <div>We couldn't find a match just yet. We will contact you if we find a match in the near future.</div>
          <div>If you would like to view the full list of services, click below:</div>
          <Button className="button-style" type="primary" href={allProgramsUrl} target="_blank">
            Visit Website
          </Button>
        </div>
      )}
    </>
  );
}
