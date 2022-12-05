import { Button, Card, Typography } from "antd";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { surveyClient } from "../client/surveyClient";
import { ProgramCard } from "../components/ProgramCard";
import { Program } from "../types";
import { None } from "../utils/None";
import "../Survey.css";

export function AllPrograms() {
  const [allPrograms, setAllPrograms] = useState<Program[]>([]);

  // This state will be used to tell if a new session start is underway
  const [isLoading, setIsLoading] = useState(false);

  // We'll need to navigate to the survey page
  const navigate = useNavigate();

  const sessionId = new URLSearchParams(window.location.search).get("sessionId");
  if (None(sessionId) || sessionId === "") {
    navigate("/");
  }

  useEffect(() => {
    const getAllPrograms = async () => {
      if (sessionId != null) {
        const allPrograms = await surveyClient.getAllPrograms(sessionId);
        setAllPrograms(allPrograms);
      } else {
        navigate("/");
      }
    };
    getAllPrograms();
  }, [allPrograms.length]);

  return (
    <>
      {allPrograms.length > 0 && (
        <div>
          {allPrograms
            .filter((p) => p != null)
            .map((p) => (
              <ProgramCard key={p.id} program={p} />
            ))}
        </div>
      )}
      {allPrograms.length === 0 && (
        <div className="card-style">
          We couldn't find any matching programs for your location just yet. We will contact you if we find a match in
          the near future.
          <div>If you would like to start over, click below:</div>
          <Button className="button-style" href="/">
            Start over
          </Button>
        </div>
      )}
    </>
  );
}
