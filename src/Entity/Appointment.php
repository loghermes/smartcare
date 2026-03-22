<?php

namespace App\Entity;

use App\Repository\AppointmentRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: AppointmentRepository::class)]
#[ORM\Table(name: 'appointment')]
#[ORM\HasLifecycleCallbacks]
class Appointment
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    // ================= RELATIONS =================

    #[ORM\ManyToOne(targetEntity: Patient::class)]
    #[ORM\JoinColumn(nullable: false, onDelete: 'CASCADE')]
    private ?Patient $patient = null;

    #[ORM\ManyToOne(targetEntity: Doctor::class)]
    #[ORM\JoinColumn(nullable: false, onDelete: 'CASCADE')]
    private ?Doctor $doctor = null;

    #[ORM\ManyToOne(targetEntity: Service::class)]
    #[ORM\JoinColumn(nullable: false)]
    private ?Service $service = null;

    // ================= APPOINTMENT DATA =================

    #[ORM\Column(type: 'datetime_immutable')]
    #[Assert\NotBlank]
    #[Assert\GreaterThan('now')]
    private ?\DateTimeImmutable $appointmentDate = null;

    #[ORM\Column(length: 20)]
    private string $status = 'PENDING';

    #[ORM\Column(type: 'integer', nullable: true)]
    private ?int $urgencyScore = null;

    #[ORM\Column(length: 50, nullable: true)]
    private ?string $urgencyLevel = null;

    #[ORM\Column(type: 'text', nullable: true)]
    private ?string $notes = null;

    // ================= TIMESTAMPS =================

    #[ORM\Column(type: 'datetime_immutable')]
    private ?\DateTimeImmutable $createdAt = null;

    #[ORM\Column(type: 'datetime_immutable')]
    private ?\DateTimeImmutable $updatedAt = null;

    #[ORM\PrePersist]
    public function onPrePersist(): void
    {
        $this->createdAt = new \DateTimeImmutable();
        $this->updatedAt = new \DateTimeImmutable();
    }

    #[ORM\PreUpdate]
    public function onPreUpdate(): void
    {
        $this->updatedAt = new \DateTimeImmutable();
    }

    // ================= GETTERS / SETTERS =================

    public function getId(): ?int { return $this->id; }

    public function getPatient(): ?Patient { return $this->patient; }

    public function setPatient(Patient $patient): static
    {
        $this->patient = $patient;
        return $this;
    }

    public function getDoctor(): ?Doctor { return $this->doctor; }

    public function setDoctor(Doctor $doctor): static
    {
        $this->doctor = $doctor;
        return $this;
    }

    public function getService(): ?Service { return $this->service; }

    public function setService(Service $service): static
    {
        $this->service = $service;
        return $this;
    }

    public function getAppointmentDate(): ?\DateTimeImmutable { return $this->appointmentDate; }

    public function setAppointmentDate(\DateTimeImmutable $appointmentDate): static
    {
        $this->appointmentDate = $appointmentDate;
        return $this;
    }

    public function getStatus(): string { return $this->status; }

    public function setStatus(string $status): static
    {
        $this->status = $status;
        return $this;
    }

    public function getUrgencyScore(): ?int { return $this->urgencyScore; }

    public function setUrgencyScore(?int $urgencyScore): static
    {
        $this->urgencyScore = $urgencyScore;
        return $this;
    }

    public function getUrgencyLevel(): ?string { return $this->urgencyLevel; }

    public function setUrgencyLevel(?string $urgencyLevel): static
    {
        $this->urgencyLevel = $urgencyLevel;
        return $this;
    }

    public function getNotes(): ?string { return $this->notes; }

    public function setNotes(?string $notes): static
    {
        $this->notes = $notes;
        return $this;
    }
}