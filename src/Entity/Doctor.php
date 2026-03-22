<?php

namespace App\Entity;

use App\Repository\DoctorRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: DoctorRepository::class)]
#[ORM\Table(name: 'doctor')]
#[ORM\HasLifecycleCallbacks]
class Doctor
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    // ================= RELATION WITH USER =================

    #[ORM\OneToOne(targetEntity: User::class, inversedBy: 'doctor')]
    #[ORM\JoinColumn(nullable: false, onDelete: 'CASCADE')]
    private ?User $user = null;

    // ================= BASIC INFO =================

    #[ORM\Column(length: 100)]
    #[Assert\NotBlank]
    private ?string $firstName = null;

    #[ORM\Column(length: 100)]
    #[Assert\NotBlank]
    private ?string $lastName = null;

    #[ORM\Column(length: 180, unique: true)]
    private ?string $slug = null;

    #[ORM\Column(length: 20)]
    #[Assert\Choice(['male', 'female', 'other'])]
    private ?string $gender = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $profilePhoto = null;

    #[ORM\Column(length: 150)]
    #[Assert\NotBlank]
    private ?string $specialization = null;

    #[ORM\Column(length: 50, unique: true)]
    #[Assert\NotBlank]
    private ?string $licenseNumber = null;

    #[ORM\Column(length: 20)]
    #[Assert\NotBlank]
    private ?string $phone = null;

    #[ORM\Column(type: 'integer')]
    #[Assert\PositiveOrZero]
    private int $yearsExperience = 0;

    #[ORM\Column(type: 'text', nullable: true)]
    private ?string $bio = null;

    #[ORM\Column(type: 'boolean')]
    private bool $isAvailable = true;

    // ================= TIMESTAMPS =================

    #[ORM\Column(type: 'datetime_immutable')]
    private ?\DateTimeImmutable $createdAt = null;

    #[ORM\Column(type: 'datetime_immutable')]
    private ?\DateTimeImmutable $updatedAt = null;

    // ================= LIFECYCLE =================

    #[ORM\PrePersist]
    public function onPrePersist(): void
    {
        $this->createdAt = new \DateTimeImmutable();
        $this->updatedAt = new \DateTimeImmutable();
        $this->generateSlug();
    }

    #[ORM\PreUpdate]
    public function onPreUpdate(): void
    {
        $this->updatedAt = new \DateTimeImmutable();
        $this->generateSlug();
    }

    private function generateSlug(): void
    {
        if ($this->firstName && $this->lastName) {
            $base = strtolower($this->firstName . '-' . $this->lastName);
            $base = preg_replace('/[^a-z0-9]+/', '-', $base);
            $this->slug = 'dr-' . trim($base, '-');
        }
    }

    // ================= GETTERS / SETTERS =================

    public function getId(): ?int { return $this->id; }

    public function getUser(): ?User { return $this->user; }
    public function setUser(User $user): static { $this->user = $user; return $this; }

    public function getFirstName(): ?string { return $this->firstName; }
    public function setFirstName(string $firstName): static
    {
        $this->firstName = ucfirst(strtolower($firstName));
        return $this;
    }

    public function getLastName(): ?string { return $this->lastName; }
    public function setLastName(string $lastName): static
    {
        $this->lastName = strtoupper($lastName);
        return $this;
    }

    public function getFullName(): string
    {
        return $this->firstName . ' ' . $this->lastName;
    }

    public function getSlug(): ?string { return $this->slug; }

    public function getGender(): ?string { return $this->gender; }
    public function setGender(?string $gender): static
    {
        $this->gender = $gender;
        return $this;
    }

    public function getProfilePhoto(): ?string { return $this->profilePhoto; }
    public function setProfilePhoto(?string $profilePhoto): static
    {
        $this->profilePhoto = $profilePhoto;
        return $this;
    }

    public function getSpecialization(): ?string { return $this->specialization; }
    public function setSpecialization(string $specialization): static
    {
        $this->specialization = $specialization;
        return $this;
    }

    public function getLicenseNumber(): ?string { return $this->licenseNumber; }
    public function setLicenseNumber(string $licenseNumber): static
    {
        $this->licenseNumber = $licenseNumber;
        return $this;
    }

    public function getPhone(): ?string { return $this->phone; }
    public function setPhone(string $phone): static
    {
        $this->phone = $phone;
        return $this;
    }

    public function getYearsExperience(): int { return $this->yearsExperience; }
    public function setYearsExperience(int $yearsExperience): static
    {
        $this->yearsExperience = $yearsExperience;
        return $this;
    }

    public function getBio(): ?string { return $this->bio; }
    public function setBio(?string $bio): static
    {
        $this->bio = $bio;
        return $this;
    }

    public function isAvailable(): bool { return $this->isAvailable; }
    public function setIsAvailable(bool $isAvailable): static
    {
        $this->isAvailable = $isAvailable;
        return $this;
    }

    public function getCreatedAt(): ?\DateTimeImmutable { return $this->createdAt; }
    public function getUpdatedAt(): ?\DateTimeImmutable { return $this->updatedAt; }
}